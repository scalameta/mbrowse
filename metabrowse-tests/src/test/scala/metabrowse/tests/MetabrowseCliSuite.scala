package metabrowse.tests

import java.nio.file._
import metabrowse.{schema => d}
import scala.meta.internal.io.FileIO
import GeneratedSiteEnrichments._

class MetabrowseCliSuite extends BaseMetabrowseCliSuite {

  val is212 = scala.util.Properties.versionNumberString.startsWith("2.12.")
  val scalaVersionDir = if (is212) "scala-2.12-" else "scala-2.13+"
  val expectedFiles =
    s"""paiges/core/src/main/$scalaVersionDir/org/typelevel/paiges/ScalaVersionCompat.scala.semanticdb.gz
      |paiges/core/src/main/scala/org/typelevel/paiges/Chunk.scala.semanticdb.gz
      |paiges/core/src/main/scala/org/typelevel/paiges/Doc.scala.semanticdb.gz
      |paiges/core/src/main/scala/org/typelevel/paiges/Document.scala.semanticdb.gz
      |paiges/core/src/main/scala/org/typelevel/paiges/Style.scala.semanticdb.gz
      |paiges/core/src/main/scala/org/typelevel/paiges/package.scala.semanticdb.gz
      |paiges/core/src/test/scala/org/typelevel/paiges/ColorTest.scala.semanticdb.gz
      |paiges/core/src/test/scala/org/typelevel/paiges/DocumentTests.scala.semanticdb.gz
      |paiges/core/src/test/scala/org/typelevel/paiges/Generators.scala.semanticdb.gz
      |paiges/core/src/test/scala/org/typelevel/paiges/JsonTest.scala.semanticdb.gz
      |paiges/core/src/test/scala/org/typelevel/paiges/PaigesScalacheckTest.scala.semanticdb.gz
      |paiges/core/src/test/scala/org/typelevel/paiges/PaigesTest.scala.semanticdb.gz""".stripMargin
  test("semanticdb.gz") {
    val obtained = FileIO
      .listAllFilesRecursively(out.resolve("semanticdb"))
      .files
      .sortBy(_.toString())
    assertNoDiffOrPrintExpected(obtained.mkString("\n"), expectedFiles)
  }

  test("package.symbolindexes.gz") {
    val expectedSymbols = "org/typelevel/paiges/package.symbolindexes.gz"
    val obtained = FileIO
      .listAllFilesRecursively(out.resolve("symbol"))
      .files
      .sortBy(_.toString())
    assertNoDiffOrPrintExpected(obtained.mkString("\n"), expectedSymbols)
  }

  test("indexed symbols") {
    val obtained = FileIO
      .listAllFilesRecursively(out.resolve("symbol"))
      .flatMap { path =>
        d.SymbolIndexes.parseFromCompressedPath(path).indexes
      }
      .map { sym =>
        assert(sym.definition.isDefined)
        sym.symbol
      }
      .sorted
      .mkString("\n")
    val extra =
      if (is212)
        """org/typelevel/paiges/ScalaVersionCompat.LazyList#
          |org/typelevel/paiges/ScalaVersionCompat.LazyList.
          |""".stripMargin
      else ""
    val expected =
      s"""|org/typelevel/paiges/Chunk.
        |org/typelevel/paiges/Chunk.best().
        |org/typelevel/paiges/Chunk.indentMax.
        |org/typelevel/paiges/Chunk.indentTable.
        |org/typelevel/paiges/Chunk.lineToStr().
        |org/typelevel/paiges/Chunk.makeIndentStr().
        |org/typelevel/paiges/ColorTest#
        |org/typelevel/paiges/ColorTest#Quote.
        |org/typelevel/paiges/ColorTest#`<init>`().
        |org/typelevel/paiges/ColorTest#bg().
        |org/typelevel/paiges/ColorTest#fg().
        |org/typelevel/paiges/ColorTest#fromAngle().
        |org/typelevel/paiges/ColorTest#rainbow().
        |org/typelevel/paiges/Doc#
        |org/typelevel/paiges/Doc#`&:`().
        |org/typelevel/paiges/Doc#`&`().
        |org/typelevel/paiges/Doc#`*`().
        |org/typelevel/paiges/Doc#`+:`().
        |org/typelevel/paiges/Doc#`+`().
        |org/typelevel/paiges/Doc#`/:`().
        |org/typelevel/paiges/Doc#`/`().
        |org/typelevel/paiges/Doc#`:&`().
        |org/typelevel/paiges/Doc#`:+`().
        |org/typelevel/paiges/Doc#`:/`().
        |org/typelevel/paiges/Doc#`<init>`().
        |org/typelevel/paiges/Doc#aligned().
        |org/typelevel/paiges/Doc#bracketBy().
        |org/typelevel/paiges/Doc#flatten().
        |org/typelevel/paiges/Doc#flattenBoolean().
        |org/typelevel/paiges/Doc#flattenOption().
        |org/typelevel/paiges/Doc#grouped().
        |org/typelevel/paiges/Doc#hang().
        |org/typelevel/paiges/Doc#hashCode.
        |org/typelevel/paiges/Doc#indent().
        |org/typelevel/paiges/Doc#isEmpty().
        |org/typelevel/paiges/Doc#line().
        |org/typelevel/paiges/Doc#line(+1).
        |org/typelevel/paiges/Doc#lineOrSpace().
        |org/typelevel/paiges/Doc#lineOrSpace(+1).
        |org/typelevel/paiges/Doc#maxWidth().
        |org/typelevel/paiges/Doc#nested().
        |org/typelevel/paiges/Doc#nonEmpty().
        |org/typelevel/paiges/Doc#render().
        |org/typelevel/paiges/Doc#renderGen().
        |org/typelevel/paiges/Doc#renderStream().
        |org/typelevel/paiges/Doc#renderStreamTrim().
        |org/typelevel/paiges/Doc#renderTrim().
        |org/typelevel/paiges/Doc#renderWideStream().
        |org/typelevel/paiges/Doc#repeat().
        |org/typelevel/paiges/Doc#representation().
        |org/typelevel/paiges/Doc#space().
        |org/typelevel/paiges/Doc#space(+1).
        |org/typelevel/paiges/Doc#style().
        |org/typelevel/paiges/Doc#tightBracketBy().
        |org/typelevel/paiges/Doc#toString().
        |org/typelevel/paiges/Doc#unzero().
        |org/typelevel/paiges/Doc#writeTo().
        |org/typelevel/paiges/Doc#writeToGen().
        |org/typelevel/paiges/Doc#writeToTrim().
        |org/typelevel/paiges/Doc.
        |org/typelevel/paiges/Doc.Align#
        |org/typelevel/paiges/Doc.Align#`<init>`().
        |org/typelevel/paiges/Doc.Align#doc.
        |org/typelevel/paiges/Doc.Align.
        |org/typelevel/paiges/Doc.Concat#
        |org/typelevel/paiges/Doc.Concat#`<init>`().
        |org/typelevel/paiges/Doc.Concat#a.
        |org/typelevel/paiges/Doc.Concat#b.
        |org/typelevel/paiges/Doc.Concat.
        |org/typelevel/paiges/Doc.Empty.
        |org/typelevel/paiges/Doc.FlatAlt#
        |org/typelevel/paiges/Doc.FlatAlt#`<init>`().
        |org/typelevel/paiges/Doc.FlatAlt#default.
        |org/typelevel/paiges/Doc.FlatAlt#whenFlat.
        |org/typelevel/paiges/Doc.FlatAlt.
        |org/typelevel/paiges/Doc.LazyDoc#
        |org/typelevel/paiges/Doc.LazyDoc#`<init>`().
        |org/typelevel/paiges/Doc.LazyDoc#computed().
        |org/typelevel/paiges/Doc.LazyDoc#evaluated.
        |org/typelevel/paiges/Doc.LazyDoc#thunk.
        |org/typelevel/paiges/Doc.LazyDoc.
        |org/typelevel/paiges/Doc.Line.
        |org/typelevel/paiges/Doc.Nest#
        |org/typelevel/paiges/Doc.Nest#`<init>`().
        |org/typelevel/paiges/Doc.Nest#doc.
        |org/typelevel/paiges/Doc.Nest#indent.
        |org/typelevel/paiges/Doc.Nest.
        |org/typelevel/paiges/Doc.Text#
        |org/typelevel/paiges/Doc.Text#`<init>`().
        |org/typelevel/paiges/Doc.Text#str.
        |org/typelevel/paiges/Doc.Text.
        |org/typelevel/paiges/Doc.Union#
        |org/typelevel/paiges/Doc.Union#`<init>`().
        |org/typelevel/paiges/Doc.Union#a.
        |org/typelevel/paiges/Doc.Union#b.
        |org/typelevel/paiges/Doc.Union.
        |org/typelevel/paiges/Doc.ZeroWidth#
        |org/typelevel/paiges/Doc.ZeroWidth#`<init>`().
        |org/typelevel/paiges/Doc.ZeroWidth#str.
        |org/typelevel/paiges/Doc.ZeroWidth.
        |org/typelevel/paiges/Doc.ansiControl().
        |org/typelevel/paiges/Doc.cat().
        |org/typelevel/paiges/Doc.char().
        |org/typelevel/paiges/Doc.charTable.
        |org/typelevel/paiges/Doc.comma.
        |org/typelevel/paiges/Doc.defer().
        |org/typelevel/paiges/Doc.empty.
        |org/typelevel/paiges/Doc.equivAtWidths().
        |org/typelevel/paiges/Doc.fill().
        |org/typelevel/paiges/Doc.foldDocs().
        |org/typelevel/paiges/Doc.hardLine().
        |org/typelevel/paiges/Doc.intercalate().
        |org/typelevel/paiges/Doc.line.
        |org/typelevel/paiges/Doc.lineBreak.
        |org/typelevel/paiges/Doc.lineOr().
        |org/typelevel/paiges/Doc.lineOrEmpty.
        |org/typelevel/paiges/Doc.lineOrSpace.
        |org/typelevel/paiges/Doc.maxSpaceTable.
        |org/typelevel/paiges/Doc.orderingAtWidth().
        |org/typelevel/paiges/Doc.paragraph().
        |org/typelevel/paiges/Doc.space.
        |org/typelevel/paiges/Doc.spaceArray.
        |org/typelevel/paiges/Doc.spaces().
        |org/typelevel/paiges/Doc.split().
        |org/typelevel/paiges/Doc.splitWhitespace.
        |org/typelevel/paiges/Doc.spread().
        |org/typelevel/paiges/Doc.stack().
        |org/typelevel/paiges/Doc.str().
        |org/typelevel/paiges/Doc.tabulate().
        |org/typelevel/paiges/Doc.tabulate(+1).
        |org/typelevel/paiges/Doc.text().
        |org/typelevel/paiges/Doc.zeroWidth().
        |org/typelevel/paiges/Document#
        |org/typelevel/paiges/Document#contramap().
        |org/typelevel/paiges/Document#document().
        |org/typelevel/paiges/Document.
        |org/typelevel/paiges/Document.FromToString.
        |org/typelevel/paiges/Document.FromToString.document().
        |org/typelevel/paiges/Document.LazyDocument#
        |org/typelevel/paiges/Document.LazyDocument#`<init>`().
        |org/typelevel/paiges/Document.LazyDocument#computed().
        |org/typelevel/paiges/Document.LazyDocument#document().
        |org/typelevel/paiges/Document.LazyDocument#evaluated.
        |org/typelevel/paiges/Document.LazyDocument#thunk.
        |org/typelevel/paiges/Document.LazyDocument.
        |org/typelevel/paiges/Document.Ops#
        |org/typelevel/paiges/Document.Ops#doc().
        |org/typelevel/paiges/Document.Ops#instance().
        |org/typelevel/paiges/Document.Ops#self().
        |org/typelevel/paiges/Document.ToDocumentOps#
        |org/typelevel/paiges/Document.ToDocumentOps#toDocumentOps().
        |org/typelevel/paiges/Document.apply().
        |org/typelevel/paiges/Document.defer().
        |org/typelevel/paiges/Document.documentBoolean.
        |org/typelevel/paiges/Document.documentByte.
        |org/typelevel/paiges/Document.documentChar.
        |org/typelevel/paiges/Document.documentDouble.
        |org/typelevel/paiges/Document.documentFloat.
        |org/typelevel/paiges/Document.documentInt.
        |org/typelevel/paiges/Document.documentIterable().
        |org/typelevel/paiges/Document.documentLong.
        |org/typelevel/paiges/Document.documentShort.
        |org/typelevel/paiges/Document.documentString.
        |org/typelevel/paiges/Document.documentUnit.
        |org/typelevel/paiges/Document.instance().
        |org/typelevel/paiges/Document.ops.
        |org/typelevel/paiges/Document.useToString().
        |org/typelevel/paiges/DocumentTest#
        |org/typelevel/paiges/DocumentTest#`<init>`().
        |org/typelevel/paiges/DocumentTest#document().
        |org/typelevel/paiges/DocumentTest#generatorDrivenConfig.
        |org/typelevel/paiges/Generators.
        |org/typelevel/paiges/Generators.arbDoc.
        |org/typelevel/paiges/Generators.arbUnion.
        |org/typelevel/paiges/Generators.arbitraryStyle.
        |org/typelevel/paiges/Generators.asciiString.
        |org/typelevel/paiges/Generators.cogenDoc.
        |org/typelevel/paiges/Generators.combinators.
        |org/typelevel/paiges/Generators.doc0Gen.
        |org/typelevel/paiges/Generators.fill().
        |org/typelevel/paiges/Generators.folds().
        |org/typelevel/paiges/Generators.genAttr.
        |org/typelevel/paiges/Generators.genBg.
        |org/typelevel/paiges/Generators.genDoc.
        |org/typelevel/paiges/Generators.genDocNoFill.
        |org/typelevel/paiges/Generators.genFg.
        |org/typelevel/paiges/Generators.genGroupedUnion.
        |org/typelevel/paiges/Generators.genStyle.
        |org/typelevel/paiges/Generators.genTree().
        |org/typelevel/paiges/Generators.genUnion.
        |org/typelevel/paiges/Generators.generalString.
        |org/typelevel/paiges/Generators.isUnion().
        |org/typelevel/paiges/Generators.leftAssoc().
        |org/typelevel/paiges/Generators.maxDepth.
        |org/typelevel/paiges/Generators.shrinkDoc.
        |org/typelevel/paiges/Generators.unary.
        |org/typelevel/paiges/Json#
        |org/typelevel/paiges/Json#`<init>`().
        |org/typelevel/paiges/Json#toDoc().
        |org/typelevel/paiges/Json.
        |org/typelevel/paiges/Json.JArray#
        |org/typelevel/paiges/Json.JArray#`<init>`().
        |org/typelevel/paiges/Json.JArray#toDoc().
        |org/typelevel/paiges/Json.JArray#toVector.
        |org/typelevel/paiges/Json.JArray.
        |org/typelevel/paiges/Json.JBool#
        |org/typelevel/paiges/Json.JBool#`<init>`().
        |org/typelevel/paiges/Json.JBool#toBoolean.
        |org/typelevel/paiges/Json.JBool#toDoc().
        |org/typelevel/paiges/Json.JDouble#
        |org/typelevel/paiges/Json.JDouble#`<init>`().
        |org/typelevel/paiges/Json.JDouble#toDoc().
        |org/typelevel/paiges/Json.JDouble#toDouble.
        |org/typelevel/paiges/Json.JInt#
        |org/typelevel/paiges/Json.JInt#`<init>`().
        |org/typelevel/paiges/Json.JInt#toDoc().
        |org/typelevel/paiges/Json.JInt#toInt.
        |org/typelevel/paiges/Json.JInt.
        |org/typelevel/paiges/Json.JNull.
        |org/typelevel/paiges/Json.JNull.toDoc().
        |org/typelevel/paiges/Json.JObject#
        |org/typelevel/paiges/Json.JObject#`<init>`().
        |org/typelevel/paiges/Json.JObject#toDoc().
        |org/typelevel/paiges/Json.JObject#toMap.
        |org/typelevel/paiges/Json.JString#
        |org/typelevel/paiges/Json.JString#`<init>`().
        |org/typelevel/paiges/Json.JString#str.
        |org/typelevel/paiges/Json.JString#toDoc().
        |org/typelevel/paiges/Json.JString.
        |org/typelevel/paiges/Json.escape().
        |org/typelevel/paiges/JsonTest#
        |org/typelevel/paiges/JsonTest#`<init>`().
        |org/typelevel/paiges/OurFunSuite#
        |org/typelevel/paiges/OurFunSuite#`<init>`().
        |org/typelevel/paiges/OurFunSuite#assertDoc().
        |org/typelevel/paiges/OurFunSuite#assertEq().
        |org/typelevel/paiges/OurFunSuite#assertNeq().
        |org/typelevel/paiges/PaigesScalacheckTest#
        |org/typelevel/paiges/PaigesScalacheckTest#`<init>`().
        |org/typelevel/paiges/PaigesScalacheckTest#generatorDrivenConfig.
        |org/typelevel/paiges/PaigesScalacheckTest#removeControls().
        |org/typelevel/paiges/PaigesTest#
        |org/typelevel/paiges/PaigesTest#`<init>`().
        |org/typelevel/paiges/PaigesTest.
        |org/typelevel/paiges/PaigesTest.EquivSyntax#
        |org/typelevel/paiges/PaigesTest.EquivSyntax#`<init>`().
        |org/typelevel/paiges/PaigesTest.EquivSyntax#`===`().
        |org/typelevel/paiges/PaigesTest.EquivSyntax#lhs.
        |org/typelevel/paiges/PaigesTest.EquivSyntax#toString().
        |org/typelevel/paiges/PaigesTest.containsHardLine().
        |org/typelevel/paiges/PaigesTest.debugEq().
        |org/typelevel/paiges/PaigesTest.debugNeq().
        |org/typelevel/paiges/PaigesTest.docEquiv.
        |org/typelevel/paiges/PaigesTest.esc().
        |org/typelevel/paiges/PaigesTest.fillSpec().
        |org/typelevel/paiges/PaigesTest.repr().
        |org/typelevel/paiges/PaigesTest.slowRenderTrim().
        |org/typelevel/paiges/PaigesTest.twoRightAssociated().
        |org/typelevel/paiges/ScalaVersionCompat.
        |${extra}org/typelevel/paiges/ScalaVersionCompat.lazyListFromIterator().
        |org/typelevel/paiges/Style#
        |org/typelevel/paiges/Style#`++`().
        |org/typelevel/paiges/Style#`<init>`().
        |org/typelevel/paiges/Style#end().
        |org/typelevel/paiges/Style#start().
        |org/typelevel/paiges/Style.
        |org/typelevel/paiges/Style.Ansi.
        |org/typelevel/paiges/Style.Ansi.Attr.
        |org/typelevel/paiges/Style.Ansi.Attr.BlinkOff.
        |org/typelevel/paiges/Style.Ansi.Attr.Bold.
        |org/typelevel/paiges/Style.Ansi.Attr.BoldOff.
        |org/typelevel/paiges/Style.Ansi.Attr.Conceal.
        |org/typelevel/paiges/Style.Ansi.Attr.ConcealOff.
        |org/typelevel/paiges/Style.Ansi.Attr.CrossedOut.
        |org/typelevel/paiges/Style.Ansi.Attr.CrossedOutOff.
        |org/typelevel/paiges/Style.Ansi.Attr.Faint.
        |org/typelevel/paiges/Style.Ansi.Attr.FaintOff.
        |org/typelevel/paiges/Style.Ansi.Attr.FastBlink.
        |org/typelevel/paiges/Style.Ansi.Attr.Inverse.
        |org/typelevel/paiges/Style.Ansi.Attr.InverseOff.
        |org/typelevel/paiges/Style.Ansi.Attr.Italic.
        |org/typelevel/paiges/Style.Ansi.Attr.ItalicOff.
        |org/typelevel/paiges/Style.Ansi.Attr.SlowBlink.
        |org/typelevel/paiges/Style.Ansi.Attr.Underline.
        |org/typelevel/paiges/Style.Ansi.Attr.UnderlineOff.
        |org/typelevel/paiges/Style.Ansi.Attr.code().
        |org/typelevel/paiges/Style.Ansi.Bg.
        |org/typelevel/paiges/Style.Ansi.Bg.Black.
        |org/typelevel/paiges/Style.Ansi.Bg.Blue.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightBlack.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightBlue.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightCyan.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightGreen.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightMagenta.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightRed.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightWhite.
        |org/typelevel/paiges/Style.Ansi.Bg.BrightYellow.
        |org/typelevel/paiges/Style.Ansi.Bg.Cyan.
        |org/typelevel/paiges/Style.Ansi.Bg.Default.
        |org/typelevel/paiges/Style.Ansi.Bg.Green.
        |org/typelevel/paiges/Style.Ansi.Bg.Magenta.
        |org/typelevel/paiges/Style.Ansi.Bg.Red.
        |org/typelevel/paiges/Style.Ansi.Bg.White.
        |org/typelevel/paiges/Style.Ansi.Bg.Yellow.
        |org/typelevel/paiges/Style.Ansi.Bg.code().
        |org/typelevel/paiges/Style.Ansi.Fg.
        |org/typelevel/paiges/Style.Ansi.Fg.Black.
        |org/typelevel/paiges/Style.Ansi.Fg.Blue.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightBlack.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightBlue.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightCyan.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightGreen.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightMagenta.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightRed.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightWhite.
        |org/typelevel/paiges/Style.Ansi.Fg.BrightYellow.
        |org/typelevel/paiges/Style.Ansi.Fg.Cyan.
        |org/typelevel/paiges/Style.Ansi.Fg.Default.
        |org/typelevel/paiges/Style.Ansi.Fg.Green.
        |org/typelevel/paiges/Style.Ansi.Fg.Magenta.
        |org/typelevel/paiges/Style.Ansi.Fg.Red.
        |org/typelevel/paiges/Style.Ansi.Fg.White.
        |org/typelevel/paiges/Style.Ansi.Fg.Yellow.
        |org/typelevel/paiges/Style.Ansi.Fg.code().
        |org/typelevel/paiges/Style.Empty.
        |org/typelevel/paiges/Style.Impl#
        |org/typelevel/paiges/Style.Impl#`<init>`().
        |org/typelevel/paiges/Style.Impl#bg.
        |org/typelevel/paiges/Style.Impl#fg.
        |org/typelevel/paiges/Style.Impl#sg.
        |org/typelevel/paiges/Style.Impl#start.
        |org/typelevel/paiges/Style.Impl.
        |org/typelevel/paiges/Style.Reset.
        |org/typelevel/paiges/Style.XTerm.
        |org/typelevel/paiges/Style.XTerm.Api#
        |org/typelevel/paiges/Style.XTerm.Api#`<init>`().
        |org/typelevel/paiges/Style.XTerm.Api#color().
        |org/typelevel/paiges/Style.XTerm.Api#colorCode().
        |org/typelevel/paiges/Style.XTerm.Api#fromLine().
        |org/typelevel/paiges/Style.XTerm.Api#gray().
        |org/typelevel/paiges/Style.XTerm.Api#laxColor().
        |org/typelevel/paiges/Style.XTerm.Api#laxColorCode().
        |org/typelevel/paiges/Style.XTerm.Api#laxGray().
        |org/typelevel/paiges/Style.XTerm.Api#start().
        |org/typelevel/paiges/Style.XTerm.Bg.
        |org/typelevel/paiges/Style.XTerm.Bg.fromLine().
        |org/typelevel/paiges/Style.XTerm.Bg.start.
        |org/typelevel/paiges/Style.XTerm.Fg.
        |org/typelevel/paiges/Style.XTerm.Fg.fromLine().
        |org/typelevel/paiges/Style.XTerm.Fg.start.
        |org/typelevel/paiges/Style.genCodes().
        |org/typelevel/paiges/package.
        |org/typelevel/paiges/package.call().""".stripMargin

    assertNoDiffOrPrintExpected(obtained, expected)
  }

  test("index.workspace.gz") {
    val workspacePath = out.resolve("index.workspace.gz")
    assert(Files.exists(workspacePath.toNIO))
    val workspace = d.Workspace.parseFromCompressedPath(workspacePath)
    assert(workspace.filenames.nonEmpty)
    expectedFiles.linesIterator.filter(_.trim.nonEmpty).foreach { file =>
      assert(workspace.filenames.contains(file.stripSuffix(".semanticdb.gz")))
    }
  }

  checkSymbolIndex(
    "org/typelevel/paiges/Json.JArray.",
    """symbol: "org/typelevel/paiges/Json.JArray."
      |definition {
      |  filename: "paiges/core/src/test/scala/org/typelevel/paiges/JsonTest.scala"
      |  startLine: 37
      |  startCharacter: 13
      |  endLine: 37
      |  endCharacter: 19
      |}
      |references {
      |  key: "paiges/core/src/test/scala/org/typelevel/paiges/JsonTest.scala"
      |  value {
      |    ranges {
      |      startLine: 58
      |      startCharacter: 16
      |      endLine: 58
      |      endCharacter: 22
      |    }
      |    ranges {
      |      startLine: 59
      |      startCharacter: 16
      |      endLine: 59
      |      endCharacter: 22
      |    }
      |  }
      |}
      |""".stripMargin
  )

  checkSymbolIndex(
    "org/typelevel/paiges/Json.JArray#",
    """symbol: "org/typelevel/paiges/Json.JArray#"
      |definition {
      |  filename: "paiges/core/src/test/scala/org/typelevel/paiges/JsonTest.scala"
      |  startLine: 37
      |  startCharacter: 13
      |  endLine: 37
      |  endCharacter: 19
      |}
      |references {
      |  key: "paiges/core/src/test/scala/org/typelevel/paiges/JsonTest.scala"
      |  value {
      |    ranges {
      |      startLine: 58
      |      startCharacter: 16
      |      endLine: 58
      |      endCharacter: 22
      |    }
      |    ranges {
      |      startLine: 59
      |      startCharacter: 16
      |      endLine: 59
      |      endCharacter: 22
      |    }
      |  }
      |}
      |""".stripMargin
  )

  test("--clean-target-first") {
    runCli() // assert no error
  }

}
