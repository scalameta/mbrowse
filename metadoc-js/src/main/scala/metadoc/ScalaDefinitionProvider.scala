package metadoc

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import monaco.editor.IReadOnlyModel
import monaco.languages.DefinitionProvider
import monaco.languages.Location
import monaco.CancellationToken
import monaco.Position

class ScalaDefinitionProvider(index: MetadocSemanticdbIndex)
    extends DefinitionProvider {
  override def provideDefinition(
      model: IReadOnlyModel,
      position: Position,
      token: CancellationToken
  ) = {
    def empty = Future.successful(js.Array[Location]())
    for {
      sym <- index.fetchSymbol(
        position.lineNumber.toInt,
        position.column.toInt
      )
      locations <- {
        sym
          .map(_.definition)
          .fold(empty) {
            case Some(defn) =>
              for {
                model <- MetadocTextModelService.modelReference(defn.filename)
              } yield {
                val location =
                  model.`object`.textEditorModel.resolveLocation(defn)
                js.Array[Location](location)
              }
            case None => empty
          }
      }
    } yield locations
  }.toMonacoThenable
}
