package metabrowse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import monaco.editor.ITextModel
import monaco.languages.Location
import monaco.languages.ReferenceContext
import monaco.languages.ReferenceProvider
import monaco.CancellationToken
import monaco.Position
import MetabrowseEnrichments._

class ScalaReferenceProvider(index: MetabrowseSemanticdbIndex)
    extends ReferenceProvider {
  override def provideReferences(
      model: ITextModel,
      position: Position,
      context: ReferenceContext,
      token: CancellationToken
  ) = {
    val offset = model.getOffsetAt(position).toInt
    for {
      sym <- index.fetchSymbol(offset)
    } yield {
      val references = sym.map(_.references).getOrElse(Map.empty)
      val locations = references.flatMap { case (filename, ranges) =>
        ranges.ranges.map(_.toPosition(filename)).map(resolveLocation)
      }
      js.Array[Location](locations.toSeq: _*)
    }
  }.toMonacoThenable
}
