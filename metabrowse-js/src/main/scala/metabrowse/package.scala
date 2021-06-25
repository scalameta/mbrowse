import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import metabrowse.{schema => d}
import monaco.Promise
import monaco.Range
import monaco.Monaco.Thenable
import monaco.Uri
import monaco.languages.Location
import monaco.services.IResourceInput
import monaco.services.ITextEditorOptions

package object metabrowse {

  /** Instantiate a JavaScript object conforming to a
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
    // This method allows us to work with Future[T] in metabrowse and convert
    // to monaco.Promise as late as possible.
    def toMonacoPromise: Promise[T] =
      Promise.wrap(toMonacoThenable)
    def toMonacoThenable: Thenable[T] =
      future.toJSPromise.asInstanceOf[Thenable[T]]
  }

  def resolveLocation(pos: d.Position): Location = {
    val location = jsObject[Location]
    location.uri = createUri(pos.filename)
    location.range = new Range(
      pos.startLine + 1,
      pos.startCharacter + 1,
      pos.endLine + 1,
      pos.endCharacter + 1
    )
    location
  }
}
