package metabrowse.server

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/** A sourcepath contains `*.{class,java,scala}` files of a project.
  *
  * @param classpath the regular JVM classpath of this project, containing
  *                  `*.class` files that can be used to compile the project.
  *                  Must match the `-classpath` argument passed to the Scala
  *                  compiler.
  * @param sources the accompanying sources.jar files for this project that
  *                are published under the "sources" classifier.
  */
case class Sourcepath(classpath: List[Path], sources: List[Path])

object Sourcepath {

  /** Use coursier to fetch the classpath and sources of an artifact.
    *
    * @param artifact the artifact name, for example
    *                 - org.scala-lang:scala-library:2.12.7
    *                 - org.scalameta:scalameta_2.12:4.0.0
    */
  def apply(artifacts: List[String]): Sourcepath = {
    Sourcepath(
      classpath = coursierFetch(artifacts),
      sources = jdkSources().toList ++
        coursierFetch(artifacts ++ List("--classifier", "sources"))
    )
  }
  def apply(artifact: String): Sourcepath = {
    Sourcepath(List(artifact))
  }

  /** The sources of the JDK, for example `java/lang/String.java` */
  def jdkSources(): Option[Path] = {
    for {
      javaHome <- sys.props.get("java.home")
      srcZip = Paths.get(javaHome).getParent.resolve("src.zip")
      if Files.isRegularFile(srcZip)
    } yield srcZip
  }

  private[metabrowse] def coursierFetchCompilerPlugin(
      artifact: String
  ): Path = {
    coursierFetch(List("--intransitive", artifact)).headOption.getOrElse {
      sys.error(artifact)
    }
  }
  private[metabrowse] def coursierFetch(extra: List[String]): List[Path] = {
    sys.process
      .Process(List("coursier", "fetch") ++ extra)
      .!!
      .trim
      .linesIterator
      .map(jar => Paths.get(jar))
      .toList
  }
}
