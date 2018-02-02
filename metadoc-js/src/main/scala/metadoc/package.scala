import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import metadoc.{schema => d}
import monaco.Promise
import monaco.Range
import monaco.Thenable
import monaco.Uri
import monaco.editor.IReadOnlyModel
import monaco.languages.Location
import monaco.services.IResourceInput
import monaco.services.ITextEditorOptions

package object metadoc {

  /**
    * Instantiate a JavaScript object conforming to a
    * given facade. Main usage is to create an empty
    * object and update its mutable fields.
    *
    * @example
    * {{{
    * @js.native
    * trait Point extends js.Object {
    *   var x: Int = js.native
    *   var y: Int = js.native
    * }
    *
    * val point = jsObject[Point]
    * point.x = 42
    * point.y = 21
    * }}}
    */
  def jsObject[T <: js.Object]: T =
    (new js.Object()).asInstanceOf[T]

  def createUri(filename: String): Uri =
    Uri.parse(s"semanticdb:$filename")

  implicit class XtensionFutureToThenable[T](future: Future[T]) {
    import scala.scalajs.js.JSConverters._
    // This method allows us to work with Future[T] in metadoc and convert
    // to monaco.Promise as late as possible.
    def toMonacoPromise: Promise[T] =
      Promise.wrap(toMonacoThenable)
    def toMonacoThenable: Thenable[T] =
      future.toJSPromise.asInstanceOf[Thenable[T]]
  }

  implicit class XtensionIReadOnlyModel(val self: IReadOnlyModel)
      extends AnyVal {
    def resolveLocation(pos: d.Position): Location = {
      val range = new Range(
        pos.startLine,
        pos.startCharacter,
        pos.endLine,
        pos.endCharacter
      )
      val uri = createUri(pos.filename)
      val location = new Location(uri, range)
      location
    }
  }
}
