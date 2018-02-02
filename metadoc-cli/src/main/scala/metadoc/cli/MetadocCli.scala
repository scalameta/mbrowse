package metadoc.cli

import java.io.File
import java.io.OutputStreamWriter
import java.io.PrintStream
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardOpenOption
import java.util.zip.ZipInputStream
import scala.collection.{GenSeq, concurrent}
import java.nio.file.attribute.BasicFileAttributes
import java.util
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator
import java.util.function.{Function => JFunction}
import scala.collection.parallel.mutable.ParArray
import scala.util.control.NonFatal
import caseapp._
import caseapp.core.Messages
import metadoc.schema
import metadoc.schema.SymbolIndex
import metadoc.{schema => d}
import org.langmeta._
import scalapb.json4s.JsonFormat
import scala.meta.internal.{semanticdb3 => s}
import scala.meta.internal.semanticdb3.SymbolOccurrence.{Role => r}

@AppName("metadoc")
@AppVersion("0.1.0-SNAPSHOT")
@ProgName("metadoc")
case class MetadocOptions(
    @HelpMessage(
      "The output directory to generate the metadoc site. (required)"
    )
    target: Option[String] = None,
    @HelpMessage(
      "Clean the target directory before generating new site. " +
        "All files will be deleted so be careful."
    )
    cleanTargetFirst: Boolean = false,
    @HelpMessage(
      "Experimental. Emit metadoc.zip file instead of static files."
    )
    zip: Boolean = false,
    @HelpMessage("Disable fancy progress bar")
    nonInteractive: Boolean = false
) {
  def targetPath: AbsolutePath = AbsolutePath(target.get)
}

case class Target(target: AbsolutePath, onClose: () => Unit)

class CliRunner(classpath: Seq[AbsolutePath], options: MetadocOptions) {
  val Target(target, onClose) = if (options.zip) {
    // For large corpora (>1M LOC) writing the symbol/ directory is the
    // bottle-neck unless --zip is enabled.
    val out = options.targetPath.resolve("metadoc.zip")
    Files.createDirectories(out.toNIO.getParent)
    val zipfs = FileSystems.newFileSystem(
      URI.create(s"jar:file:${out.toURI.getPath}"), {
        val env = new util.HashMap[String, String]()
        env.put("create", "true")
        env
      }
    )
    Target(AbsolutePath(zipfs.getPath("/")), () => zipfs.close())
  } else {
    Target(options.targetPath, () => ())
  }
  private val display = new TermDisplay(
    new OutputStreamWriter(System.out),
    fallbackMode = options.nonInteractive || TermDisplay.defaultFallbackMode
  )
  private val semanticdb = target.resolve("semanticdb")
  private val symbolRoot = target.resolve("symbol")
  private type Symbol = String
  private val filenames = new ConcurrentSkipListSet[String]()
  private val symbols =
    new ConcurrentHashMap[Symbol, AtomicReference[d.SymbolIndex]]()
  private val mappingFunction: JFunction[Symbol, AtomicReference[
    d.SymbolIndex
  ]] =
    t => new AtomicReference(d.SymbolIndex(symbol = t))

  private def overwrite(out: Path, bytes: Array[Byte]): Unit = {
    Files.write(
      out,
      bytes,
      StandardOpenOption.TRUNCATE_EXISTING,
      StandardOpenOption.CREATE
    )
  }

  private def addDefinition(symbol: Symbol, position: d.Position): Unit = {
    val value = symbols.computeIfAbsent(symbol, mappingFunction)
    value.getAndUpdate(new UnaryOperator[d.SymbolIndex] {
      override def apply(t: schema.SymbolIndex): schema.SymbolIndex =
        t.definition.fold(t.copy(definition = Some(position))) { _ =>
          // Do nothing, conflicting symbol definitions, for example js/jvm
          t
        }
    })
  }

  private def addReference(
      filename: String,
      range: d.Range,
      symbol: Symbol
  ): Unit = {
    val value = symbols.computeIfAbsent(symbol, mappingFunction)
    value.getAndUpdate(new UnaryOperator[d.SymbolIndex] {
      override def apply(t: d.SymbolIndex): d.SymbolIndex = {
        val ranges = t.references.getOrElse(filename, d.Ranges())
        val newRanges = ranges.copy(ranges.ranges :+ range)
        val newReferences = t.references + (filename -> newRanges)
        t.copy(references = newReferences)
      }
    })
  }

  type Tick = () => Unit

  private def phase[T](task: String, length: Int)(f: Tick => T): T = {
    display.startTask(task, new File("target"))
    display.taskLength(task, length, 0)
    val counter = new AtomicInteger()
    val tick: Tick = { () =>
      display.taskProgress(task, counter.incrementAndGet())
    }
    val result = f(tick)
    display.completedTask(task, success = true)
    result
  }

  def scanSemanticdbs(): GenSeq[AbsolutePath] =
    phase("Scanning semanticdb files", classpath.length) { tick =>
      val files = ParArray.newBuilder[AbsolutePath]
      val visitor = new SimpleFileVisitor[Path] {
        override def visitFile(
            file: Path,
            attrs: BasicFileAttributes
        ): FileVisitResult = {
          val filename = file.getFileName.toString
          if (filename.endsWith(".semanticdb") ||
            filename.endsWith(".semanticdb.json")) {
            files += AbsolutePath(file)
          }
          FileVisitResult.CONTINUE
        }
      }
      classpath.foreach { path =>
        tick()
        Files.walkFileTree(path.toNIO, visitor)
      }
      files.result()
    }

  private def parseTextDocuments(path: AbsolutePath): s.TextDocuments = {
    val filename = path.toNIO.getFileName.toString
    val bytes = path.readAllBytes
    if (filename.endsWith(".semanticdb")) {
      s.TextDocuments.parseFrom(bytes)
    } else if (filename.endsWith(".semanticdb.json")) {
      val string = new String(bytes, StandardCharsets.UTF_8)
      JsonFormat.fromJsonString[s.TextDocuments](string)
    } else {
      throw new IllegalArgumentException(s"Unexpected filename $filename")
    }
  }

  def buildSymbolIndex(paths: GenSeq[AbsolutePath]): Unit =
    phase("Building symbol index", paths.length) { tick =>
      paths.foreach { path =>
        try {
          tick()
          val docs = parseTextDocuments(path)
          docs.documents.foreach { document =>
            document.occurrences.foreach {
              case s.SymbolOccurrence(_, sym, _)
                  if !sym.endsWith(".") && !sym.endsWith("#") =>
              // Do nothing, local symbol.
              case s.SymbolOccurrence(
                  Some(
                    s.Range(startLine, startCharacter, endLine, endCharacter)
                  ),
                  sym,
                  r.DEFINITION
                  ) =>
                addDefinition(
                  sym,
                  d.Position(
                    document.uri,
                    startLine,
                    startCharacter,
                    endLine,
                    endCharacter
                  )
                )
              case s.SymbolOccurrence(
                  Some(
                    s.Range(startLine, startCharacter, endLine, endCharacter)
                  ),
                  sym,
                  r.REFERENCE
                  ) =>
                addReference(
                  document.uri,
                  d.Range(startLine, startCharacter, endLine, endCharacter),
                  sym
                )
              case _ =>
            }
            val out = semanticdb.resolve(document.uri)
            Files.createDirectories(out.toNIO.getParent)
            overwrite(
              out.toNIO
                .resolveSibling(
                  out.toNIO.getFileName.toString + ".semanticdb"
                ),
              s.TextDocuments(document :: Nil).toByteArray
            )
            filenames.add(document.uri)
          }
        } catch {
          case NonFatal(e) =>
            System.err.println(s"$path")
            val shortTrace = e.getStackTrace.take(10)
            e.setStackTrace(shortTrace)
            e.printStackTrace(new PrintStream(System.err))
        }
      }
    }

  def writeSymbolIndex(): Unit =
    phase("Writing symbol index", symbols.size()) { tick =>
      import scala.collection.JavaConverters._
      Files.createDirectory(symbolRoot.toNIO)
      val symbolsMap = symbols.asScala
      symbolsMap.foreach {
        case (_, ref) =>
          tick()
          val symbolIndex = ref.get()
          val actualIndex = symbolIndex.definition match {
            case Some(_) => updateReferencesForType(symbolsMap, symbolIndex)
            case None => updateDefinitionsForTerm(symbolsMap, symbolIndex)
          }

          if (actualIndex.definition.isDefined) {
            val url = MetadocCli.encodeSymbolName(actualIndex.symbol)
            val out = symbolRoot.resolve(url)
            overwrite(out.toNIO, actualIndex.toByteArray)
          }
      }
    }

  private def updateReferencesForType(
      symbolsMap: concurrent.Map[Symbol, AtomicReference[SymbolIndex]],
      symbolIndex: SymbolIndex
  ) = {
    Symbol(symbolIndex.symbol) match {
      case Symbol.Global(owner, Signature.Type(name)) =>
        (for {
          syntheticObjRef <- symbolsMap.get(
            Symbol.Global(owner, Signature.Term(name)).syntax
          )
          if syntheticObjRef.get().definition.isEmpty
        } yield
          symbolIndex.copy(references = syntheticObjRef.get().references))
          .getOrElse(symbolIndex)
      case _ => symbolIndex
    }
  }

  private def updateDefinitionsForTerm(
      symbolsMap: concurrent.Map[Symbol, AtomicReference[SymbolIndex]],
      symbolIndex: SymbolIndex
  ) = {
    Symbol(symbolIndex.symbol) match {
      case Symbol.Global(owner, Signature.Term(name)) =>
        (for {
          typeRef <- symbolsMap.get(
            Symbol.Global(owner, Signature.Type(name)).syntax
          )
          definition <- typeRef.get().definition
        } yield symbolIndex.copy(definition = Some(definition)))
          .getOrElse(symbolIndex)
      case _ => symbolIndex
    }
  }

  def writeAssets(): Unit = {
    val root = target.toNIO
    val inputStream = MetadocCli.getClass.getClassLoader
      .getResourceAsStream("metadoc-assets.zip")
    if (inputStream == null)
      sys.error("Failed to locate metadoc-assets.zip on the classpath")
    val zipStream = new ZipInputStream(inputStream)
    val bytes = new Array[Byte](8012)
    Stream
      .continually(zipStream.getNextEntry)
      .takeWhile(_ != null)
      .filterNot(_.isDirectory)
      .foreach { entry =>
        val path = root.resolve(entry.getName)
        if (Files.notExists(path))
          Files.createDirectories(path.getParent)
        val out = Files.newOutputStream(path, StandardOpenOption.CREATE)

        def copyLoop(): Unit = {
          val read = zipStream.read(bytes, 0, bytes.length)
          if (read > 0) {
            out.write(bytes, 0, read)
            copyLoop()
          }
        }

        copyLoop()
        out.flush()
        out.close()
      }
  }

  def writeWorkspace(): Unit = {
    import scala.collection.JavaConverters._
    val workspace = d.Workspace(filenames.asScala.toSeq)
    overwrite(target.resolve("index.workspace").toNIO, workspace.toByteArray)
  }

  def run(): Unit = {
    try {
      display.init()
      Files.createDirectories(target.toNIO)
      val paths = scanSemanticdbs()
      buildSymbolIndex(paths)
      writeSymbolIndex()
      writeAssets()
      writeWorkspace()
    } finally {
      display.stop()
      onClose()
    }
  }
}

object MetadocCli extends CaseApp[MetadocOptions] {

  override val messages: Messages[MetadocOptions] =
    Messages[MetadocOptions].copy(optionsDesc = "[options] classpath")

  def encodeSymbolName(name: String): String = {
    val md = java.security.MessageDigest.getInstance("SHA-512")
    val sha = md.digest(name.getBytes("UTF-8"))
    // 512 bits ~> 64 bytes and doubled for the hex encoding
    String.format("%0128x", new java.math.BigInteger(1, sha))
  }

  def run(options: MetadocOptions, remainingArgs: RemainingArgs): Unit = {

    if (options.target.isEmpty) {
      error("--target is required")
    }

    if (options.cleanTargetFirst) {
      import better.files._
      val file = options.targetPath.toFile.toScala
      if (file.exists) file.delete()
    }

    val classpath = remainingArgs.remainingArgs.flatMap { cp =>
      cp.split(File.pathSeparator).map(AbsolutePath(_))
    }
    val runner = new CliRunner(classpath, options)
    runner.run()
    println(options.target.get)
  }
}
