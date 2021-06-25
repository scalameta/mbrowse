package monaco

import scala.scalajs.js
import monaco.editor.{IEditor, IEditorOptions, ITextModel}
import monaco.editor.Editor.IEditorViewState

/** Service declarations to hook into the Monaco Editor.
  *
  * Out of the box the Monaco Editor uses a simple set of
  * [[services https://github.com/Microsoft/vscode/blob/a460624f88b4dd3a03d9633e0860c0078c00462f/src/vs/editor/standalone/browser/simpleServices.ts]]
  * which do not provide the ability out of the box to load content dynamically.
  *
  * The default services can be overridden using [[monaco.editor.IEditorOverrideServices]].
  * The API in inside this package contains the definitions for these services.
  *
  * Based on declarations defined in
  * [[editor.ts https://github.com/Microsoft/vscode/blob/c67ef57cda90b5f28499646f7cc94e8dcc5b0586/src/vs/platform/editor/common/editor.ts]]
  * and [[resolverService.ts https://github.com/Microsoft/vscode/blob/337ded059ae5140b86caf07e67ce92a41a8e6581/src/vs/editor/common/services/resolverService.ts]].
  */
package services {

  trait IReference[T] extends IDisposable {
    def `object`: T
  }

  class ImmortalReference[T](override val `object`: T) extends IReference[T] {
    override def dispose(): Unit = ()
  }

  @js.native
  trait ITextEditorOptions extends IEditorOptions {
    var selection: js.UndefOr[IRange] = js.native
    var viewState: js.UndefOr[IEditorViewState]
    var revealInCenterIfOutsideViewport: js.UndefOr[Boolean]
  }

  @js.native
  trait IBaseResourceInput extends js.Object {
    var options: ITextEditorOptions = js.native
    var label: String = js.native
    var description: String = js.native
  }

  @js.native
  trait IResourceInput extends IBaseResourceInput {
    var resource: Uri = js.native
    var encoding: String = js.native
  }

  @js.native
  trait IUntitledResourceInput extends IBaseResourceInput {
    var resource: Uri = js.native
    var filePath: String = js.native
    var language: String = js.native
    var contents: String = js.native
    var encoding: String = js.native
  }

  @js.native
  trait IResourceDiffInput extends IBaseResourceInput {
    var leftResource: Uri = js.native
    var rightResource: Uri = js.native
  }

  @js.native
  trait IResourceSideBySideInput extends IBaseResourceInput {
    var masterResource: Uri = js.native
    var detailResource: Uri = js.native
  }

  @js.native
  trait IEditorControl extends js.Object {}

  trait IEditorService extends js.Object {
    def openEditor(
        input: IResourceInput,
        sideBySide: js.UndefOr[Boolean] = js.undefined
    ): Promise[IEditor]
  }

  /** Service to dynamically load models.
    */
  trait ITextModelService extends js.Object {
    def createModelReference(
        resource: Uri
    ): Promise[IReference[ITextEditorModel]]
  }

  trait ITextEditorModel extends js.Object {
    def textEditorModel: ITextModel
  }

  object ITextEditorModel {
    def apply(model: ITextModel): ITextEditorModel = new ITextEditorModel {
      override def textEditorModel: ITextModel = model
    }
  }
}
