package metabrowse

import metabrowse.schema.Workspace
import monaco.Uri
import monaco.editor.Editor
import monaco.editor.IActionDescriptor
import monaco.editor.ICodeEditor
import monaco.editor.ICursorSelectionChangedEvent
import monaco.languages.ILanguageExtensionPoint
import monaco.services.IResourceInput
import monaco.services.ITextEditorOptions
import org.scalajs.dom
import org.scalajs.dom.experimental.Headers
import org.scalajs.dom.experimental.RequestInit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.meta.internal.{semanticdb => s}
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.JSON
import scala.scalajs.js.typedarray.TypedArrayBuffer
import scala.scalajs.js.typedarray.Uint8Array

object MetabrowseApp {
  def main(args: Array[String]): Unit = {
    val editorThemeMenuElement =
      dom.document.querySelector("#editor-theme-menu");
    val editorThemeMenu = new mdc.MDCSimpleMenu(editorThemeMenuElement);
    val editorThemeIcon = dom.document.querySelector(".editor-theme");
    editorThemeIcon.addEventListener(
      "click",
      { (event: dom.Event) =>
        event.preventDefault()
        editorThemeMenu.open = !editorThemeMenu.open
      }
    )

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
      workspace <- MetabrowseFetch.workspace()
    } {
      println(s"Loaded workspace with ${workspace.filenames.length} file(s)")
      val index = new MutableBrowserIndex(MetabrowseState(s.TextDocument()))
      registerLanguageExtensions(index)

      val editorService = new MetabrowseEditorService(index)
      registerWorkspaceFiles(editorService, workspace)

      dom.ext.LocalStorage("editor-theme").foreach { theme =>
        Editor.setTheme(theme)
      }

      def defaultState: Navigation.State =
        new Navigation.State(workspace.filenames.head, None)

      def locationState() =
        Navigation.parseState(Uri.parse(dom.window.location.hash).fragment)

      /*
       * Discovering the initial input state may update the history so resolve the
       * input before registering the history popstate handler to avoid any event
       * being triggered.
       */
      val initialState = locationState().getOrElse(defaultState)

      dom.window.onpopstate = { event: dom.PopStateEvent =>
        Navigation
          .fromHistoryState(event.state)
          .orElse(locationState())
          .foreach(openEditor(editorService))
      }

      dom.window.onresize = { _: dom.Event =>
        editorService.resize()
      }

      openEditor(editorService)(initialState)
    }
  }

  def updateHistory(state: Navigation.State): Unit = {
    val uri = "#/" + state.toString.dropWhile(_ == '/')

    Navigation.fromHistoryState(dom.window.history.state) match {
      case Some(cur) if cur.path == state.path =>
        dom.window.history.replaceState(state.toString, state.path, uri)
      case _ =>
        dom.window.history.pushState(state.toString, state.path, uri)
    }
  }

  def updateTitle(state: Navigation.State): Unit = {
    val title = state.path.dropWhile(_ == '/')
    dom.document.getElementById("title").textContent = title
  }

  def registerLanguageExtensions(index: MetabrowseSemanticdbIndex): Unit = {
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

  def registerWorkspaceFiles(
      editorService: MetabrowseEditorService,
      workspace: Workspace
  ): Unit = {
    workspace.filenames.foreach { file =>
      editorService.addAction(new IActionDescriptor {
        override var id = file
        override var label = file
        override def run(editor: ICodeEditor): monaco.Promise[Unit] = {
          val state = new Navigation.State(file, None)
          openEditor(editorService)(state).toMonacoPromise
        }
        override var precondition: String = ""
        override var keybindings: js.Array[Double] = js.Array()
        override var keybindingContext: String = ""
        override var contextMenuGroupId: String = ""
        override var contextMenuOrder: Double = 0
      })
    }
  }

  def openEditor(editorService: MetabrowseEditorService)(
      state: Navigation.State
  ): Future[Unit] = {
    val input = jsObject[IResourceInput]
    input.resource = createUri(state.path)
    input.options = jsObject[ITextEditorOptions]
    input.options.selection = state.selection.map(_.toRange).orUndefined

    updateHistory(state)

    for (editor <- editorService.open(input)) yield {
      updateTitle(state)

      editor.onDidChangeCursorSelection {
        cursor: ICursorSelectionChangedEvent =>
          val selection = Navigation.Selection.fromRange(cursor.selection)
          val state =
            new Navigation.State(editor.getModel().uri.path, Some(selection))
          updateTitle(state)
          updateHistory(state)
      }
    }
  }

  def fetchBytes(
      url: String,
      headers: Headers = new Headers()
  ): Future[Array[Byte]] = {
    for {
      response <- dom.experimental.Fetch
        .fetch(url, RequestInit(headers = headers))
        .toFuture
      _ = require(response.status == 200, s"${response.status} != 200")
      buffer <- response.arrayBuffer().toFuture
    } yield {
      val gzip: js.UndefOr[String] = "gzip"
      val isGzip = url.endsWith(".gz") &&
        response.headers.get("Content-Encoding") != gzip
      // NOTE(olafur) Ideally, the browser should be able to inflate the response
      // for us instead of us doing it with a JavaScript library. However, the
      // browser does not inflate the response body even if the server response
      // headers contains `Content-Type: application/gzip`. I tried
      // updating the request headers to include `Accept-Encoding: gzip` but it
      // did not help. If someone can figure out how to let the browser inflate
      // for us then that would be great. However, pako claims to be "Almost as
      // fast in modern JS engines as C implementation" so maybe it's not a problem.
      // If we go the route of letting the browser inflate for us, then we would
      // first need to guarantee that it works with any static file server.
      val output =
        if (isGzip) Pako.inflate(buffer)
        else buffer
      val bytes = Array.ofDim[Byte](output.byteLength)
      TypedArrayBuffer.wrap(output).get(bytes)
      bytes
    }
  }

  /** Load the Monaco Editor AMD bundle using `require`.
    *
    * The AMD bundle is not compatible with Webpack and must be loaded
    * dynamically at runtime to avoid errors:
    * https://github.com/Microsoft/monaco-editor/issues/18
    */
  def loadMonaco(): Future[Unit] = {
    val promise = Promise[Unit]()
    val mod = "vs/editor/editor.main"
    // call require via eval, so that webpack doesn't pick it during linking
    js.Dynamic.global.eval("require")(
      js.Array(mod),
      { ctx: js.Dynamic =>
        println("Monaco Editor loaded")
        promise.success(())
      }: js.ThisFunction
    )
    promise.future
  }

  val ScalaLanguageExtensionPoint = {
    val language = jsObject[ILanguageExtensionPoint]
    language.id = "scala"
    language
  }
}
