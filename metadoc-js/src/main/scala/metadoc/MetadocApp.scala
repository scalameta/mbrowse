package metadoc

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.scalajs.js
import scala.scalajs.js.typedarray.TypedArrayBuffer
import scala.scalajs.js.JSConverters._
import monaco.{IRange, Range, Uri}
import monaco.Uri
import monaco.editor.Editor
import monaco.editor.IEditor
import monaco.editor.IEditorConstructionOptions
import monaco.editor.IEditorOptions
import monaco.editor.IEditorOverrideServices
import monaco.editor.IModelChangedEvent
import monaco.languages.ILanguageExtensionPoint
import monaco.services.{IResourceInput, ITextEditorOptions}
import org.scalajs.dom
import scala.meta.internal.{semanticdb3 => s}

object MetadocApp {
  def main(args: Array[String]): Unit = {
    val editorThemeMenuElement =
      dom.document.querySelector("#editor-theme-menu");
    val editorThemeMenu = new mdc.MDCSimpleMenu(editorThemeMenuElement);
    val editorThemeIcon = dom.document.querySelector(".editor-theme");
    editorThemeIcon.addEventListener("click", { (event: dom.Event) =>
      event.preventDefault()
      editorThemeMenu.open = !editorThemeMenu.open
    })

    for (theme <- Seq("vs", "vs-dark", "hc-black")) {
      val themeControl = dom.document
        .getElementById(s"theme:$theme")
        .asInstanceOf[dom.html.Input]

      themeControl.onclick = { (e: dom.MouseEvent) =>
        Editor.setTheme(theme)
        dom.ext.LocalStorage.update("editor-theme", theme)
      }
    }

    for {
      _ <- loadMonaco()
      workspace <- MetadocFetch.workspace()
    } {
      val index = new MutableBrowserIndex(MetadocState(s.TextDocument()))
      registerLanguageExtensions(index)
      val editorService = new MetadocEditorService(index)

      dom.ext.LocalStorage("editor-theme").foreach { theme =>
        Editor.setTheme(theme)
      }

      def defaultState = {
        val state = Navigation.parseState(workspace.filenames.head)
        // Starting with any path, so add the file to the history
        state.foreach(updateHistory)
        state
      }

      /*
       * Discovering the initial input state may update the history so resolve the
       * input before registering the history popstate handler to avoid any event
       * being triggered.
       */
      val state =
        Navigation
          .parseState(Uri.parse(dom.window.location.hash).fragment)
          .orElse(defaultState)

      dom.window.onpopstate = { e: dom.PopStateEvent =>
        for (state <- Navigation.currentState())
          openEditor(editorService)(state)
      }

      dom.window.onresize = { _: dom.Event =>
        editorService.resize()
      }

      state.foreach(openEditor(editorService))
    }
  }

  def updateHistory(state: Navigation.State): Unit = {
    val uri = "#/" + state.toString.dropWhile(_ == '/')

    Navigation.currentState() match {
      case Some(cur) if cur.path == state.path =>
        dom.window.history.replaceState(state, state.path, uri)
      case _ =>
        dom.window.history.pushState(state, state.path, uri)
    }
  }

  def updateTitle(state: Navigation.State): Unit = {
    val title = state.path.dropWhile(_ == '/')
    dom.document.getElementById("title").textContent = title
  }

  def registerLanguageExtensions(index: MetadocSemanticdbIndex): Unit = {
    monaco.languages.Languages.register(ScalaLanguageExtensionPoint)
    monaco.languages.Languages.setMonarchTokensProvider(
      ScalaLanguageExtensionPoint.id,
      ScalaLanguage.language
    )
    monaco.languages.Languages.setLanguageConfiguration(
      ScalaLanguageExtensionPoint.id,
      ScalaLanguage.conf
    )
    monaco.languages.Languages.registerDefinitionProvider(
      ScalaLanguageExtensionPoint.id,
      new ScalaDefinitionProvider(index)
    )
    monaco.languages.Languages.registerReferenceProvider(
      ScalaLanguageExtensionPoint.id,
      new ScalaReferenceProvider(index)
    )
    monaco.languages.Languages.registerDocumentSymbolProvider(
      ScalaLanguageExtensionPoint.id,
      new ScalaDocumentSymbolProvider(index)
    )
  }

  def openEditor(editorService: MetadocEditorService)(
      state: Navigation.State
  ): Unit = {
    val input = jsObject[IResourceInput]
    input.resource = createUri(state.path)
    input.options = jsObject[ITextEditorOptions]
    input.options.selection = state.selection.map(_.toRange).orUndefined

    for (editor <- editorService.open(input)) {
      updateTitle(state)

      editor.onDidChangeCursorSelection { cursor =>
        val selection = Navigation.Selection.fromRange(cursor.selection)
        val state =
          new Navigation.State(editor.getModel().uri.path, Some(selection))
        updateHistory(state)
      }
    }
  }

  def fetchBytes(url: String): Future[Array[Byte]] = {
    for {
      response <- dom.experimental.Fetch.fetch(url).toFuture
      _ = require(response.status == 200, s"${response.status} != 200")
      buffer <- response.arrayBuffer().toFuture
    } yield {
      val bytes = Array.ofDim[Byte](buffer.byteLength)
      TypedArrayBuffer.wrap(buffer).get(bytes)
      bytes
    }
  }

  /**
    * Load the Monaco Editor AMD bundle using `require`.
    *
    * The AMD bundle is not compatible with Webpack and must be loaded
    * dynamically at runtime to avoid errors:
    * https://github.com/Microsoft/monaco-editor/issues/18
    */
  def loadMonaco(): Future[Unit] = {
    val promise = Promise[Unit]()
    js.Dynamic.global.require(js.Array("vs/editor/editor.main"), {
      ctx: js.Dynamic =>
        println("Monaco Editor loaded")
        promise.success(())
    }: js.ThisFunction)
    promise.future
  }

  val ScalaLanguageExtensionPoint = new ILanguageExtensionPoint("scala")
}
