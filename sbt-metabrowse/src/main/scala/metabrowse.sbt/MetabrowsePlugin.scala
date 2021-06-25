package metabrowse.sbt

import sbt._
import sbt.Keys._

/** Generate a staic Metabrowse site.
  *
  * == Usage ==
  *
  * This plugin must be explicitly enabled in the project that generates the
  * static metabrowse site. To enable it add the following line
  * to your `.sbt` file:
  * {{{
  * enablePlugins(MetabrowsePlugin)
  * }}}
  *
  * The static site includes sources for projects that enable the semanticdb-scalac
  * compiler plugin, see http://scalameta.org/tutorial/#semanticdb-scalac.
  * To enable the compiler plugin, add the following to your projects settings
  *
  * {{{
  *   lazy val projectToIncludeSourcesForMetabrowseSite = project.settings(
  *     metabrowseSettings // important, must *appear* after scalacOptions.
  *   )
  * }}}
  *
  * By default, semantic data is read from `metabrowseClasspath` which is
  * automatically populated based on the various filter settings.
  */
object MetabrowsePlugin extends AutoPlugin {

  object autoImport {
    val Metabrowse = config("metabrowse")
    val metabrowseSettings = MetabrowsePlugin.metabrowseSettings
    val metabrowseScopeFilter =
      settingKey[ScopeFilter]("Control sources to be included in metabrowse.")
    val metabrowseProjectFilter = settingKey[ScopeFilter.ProjectFilter](
      "Control projects to be included in metabrowse."
    )
    val metabrowseConfigurationFilter =
      settingKey[ScopeFilter.ConfigurationFilter](
        "Control configurations to be included in metabrowse."
      )
    val metabrowseClasspath =
      taskKey[Seq[Classpath]]("Class directories to be included in metabrowse")
    val metabrowse = taskKey[File]("Generate a static Metabrowse site")
  }
  import autoImport._

  override def requires = plugins.JvmPlugin
  override def trigger: PluginTrigger = allRequirements

  lazy val metabrowseSettings: List[Def.Setting[_]] = List(
    addCompilerPlugin(
      "org.scalameta" % "semanticdb-scalac" % BuildInfo.scalametaVersion cross CrossVersion.full
    ),
    scalacOptions ++= Seq(
      "-Yrangepos",
      "-Xplugin-require:semanticdb"
    )
  )

  lazy val classpathTask = Def.taskDyn {
    fullClasspath.all(metabrowseScopeFilter.value)
  }

  override def projectSettings: Seq[Setting[_]] = Seq(
    ivyConfigurations += Metabrowse,
    libraryDependencies ++= List(
      // Explicitly set the Scala version dependency so the resolution doesn't pick
      // up the Scala version of the project the plugin is enabled in.
      "org.scala-lang" % "scala-reflect" % BuildInfo.scalaVersion % Metabrowse,
      "org.scalameta" % s"metabrowse-cli_${BuildInfo.scalaBinaryVersion}" % BuildInfo.version % Metabrowse
    ),
    metabrowseClasspath := classpathTask.value,
    metabrowseScopeFilter := ScopeFilter(
      metabrowseProjectFilter.value,
      metabrowseConfigurationFilter.value
    ),
    metabrowseProjectFilter := inAnyProject,
    metabrowseConfigurationFilter := inConfigurations(Compile, Test),
    target in metabrowse := target.value / "metabrowse",
    mainClass in Metabrowse := Some("metabrowse.cli.MetabrowseCli"),
    fullClasspath in Metabrowse := Classpaths
      .managedJars(Metabrowse, classpathTypes.value, update.value),
    runner in (Metabrowse, run) := {
      new ForkRun(Compat.forkOptions.value)
    },
    metabrowse := {
      val output = (target in metabrowse).value
      val classpath = metabrowseClasspath.value.flatten
      val classDirectories = Attributed
        .data(classpath)
        .collect {
          case entry if entry.isDirectory => entry.getAbsolutePath
        }
        .distinct
      val options = Seq(
        "--clean-target-first",
        "--target",
        output.getAbsolutePath
      ) ++ classDirectories

      (runner in (Metabrowse, run)).value.run(
        (mainClass in Metabrowse).value.get,
        Attributed.data((fullClasspath in Metabrowse).value),
        options,
        streams.value.log
      )

      output
    }
  )
}
