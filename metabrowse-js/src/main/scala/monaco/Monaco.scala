package monaco

import scala.scalajs.js
import js.annotation._
import js.|
import js.RegExp

import org.scalajs.dom.{Element => HTMLElement, MouseEvent, KeyboardEvent}

import monaco.Monaco.Thenable
import monaco.languages.Languages.CharacterPair
import monaco.languages.Languages.Definition
import monaco.editor.Editor.BuiltinTheme
import monaco.editor.Editor.IColors
import monaco.editor.Editor.IEditorModel
import monaco.editor.Editor.IEditorViewState

// @js.native - NOTE: extended in MonacoServices
trait IDisposable extends js.Object {
  def dispose(): Unit // = js.native
}

@js.native
trait IEvent[T] extends js.Object {
  def apply(
      listener: js.Function1[T, Any],
      thisArg: js.Any = ???
  ): IDisposable = js.native
}

@js.native
@JSGlobal("monaco.Emitter")
class Emitter[T] extends js.Object {
  def event: IEvent[T] = js.native
  def fire(event: T = ???): Unit = js.native
  def dispose(): Unit = js.native
}

@js.native
sealed trait Severity extends js.Object {}

@js.native
@JSGlobal("monaco.Severity")
object Severity extends js.Object {
  var Ignore: Severity = js.native
  var Info: Severity = js.native
  var Warning: Severity = js.native
  var Error: Severity = js.native
  @JSBracketAccess
  def apply(value: Severity): String = js.native
}

@js.native
sealed trait MarkerSeverity extends js.Object {}

@js.native
@JSGlobal("monaco.MarkerSeverity")
object MarkerSeverity extends js.Object {
  var Hint: MarkerSeverity = js.native
  var Info: MarkerSeverity = js.native
  var Warning: MarkerSeverity = js.native
  var Error: MarkerSeverity = js.native
  @JSBracketAccess
  def apply(value: MarkerSeverity): String = js.native
}

@js.native
@JSGlobal("monaco.Promise")
// NOTE: Covariant to make things easier.
class Promise[+T] protected () extends js.Object {
  type TProgress = Unit
  /*
  def this(
      executor: js.Function3[
        js.Function1[T | PromiseLike[T], Unit],
        js.Function1[js.Any, Unit],
        js.Function1[TProgress, Unit],
        Unit
      ],
      oncancel: js.Function0[Unit] = ???
  ) = this()
  def `then`[TResult1, TResult2](
      onfulfilled: js.Function1[T, TResult1 | PromiseLike[TResult1]] | Null =
        ???,
      onrejected: js.Function1[js.Any, TResult2 | PromiseLike[TResult2]] | Null =
        ???,
      onprogress: js.Function1[TProgress, Unit] = ???
  ): Promise[TResult1 | TResult2] = js.native
   */
  def done(
      onfulfilled: js.Function1[T, Unit] = ???,
      onrejected: js.Function1[js.Any, Unit] = ???,
      onprogress: js.Function1[TProgress, Unit] = ???
  ): Unit = js.native
  def cancel(): Unit = js.native
}

@js.native
@JSGlobal("monaco.Promise")
object Promise extends js.Object {
  def as(value: Null): Promise[Null] = js.native
  def as(value: Unit): Promise[Unit] = js.native
  def as[T](value: PromiseLike[T]): PromiseLike[T] = js.native
  //def as[T, SomePromise <: PromiseLike[T]](value: SomePromise): SomePromise = js.native
  def as[T](value: T): Promise[T] = js.native
  def is(value: js.Any): Boolean = js.native
  def timeout(delay: Double): Promise[Unit] = js.native
  def join[T1, T2](
      promises: js.Tuple2[T1 | PromiseLike[T1], T2 | PromiseLike[T2]]
  ): Promise[js.Tuple2[T1, T2]] = js.native
  def join[T](
      promises: js.Array[T | PromiseLike[T]]
  ): Promise[js.Array[T]] = js.native
  def join[T](
      promises: js.Dictionary[T | PromiseLike[T]]
  ): Promise[js.Dictionary[T]] = js.native
  def any[T](
      promises: js.Array[T | PromiseLike[T]]
  ): Promise[js.Any] = js.native
  def wrap[T](value: PromiseLike[T]): Promise[T] = js.native
  def wrapError[T](error: Error): Promise[T] = js.native
}

@js.native
@JSGlobal("monaco.CancellationTokenSource")
class CancellationTokenSource extends js.Object {
  def token: CancellationToken = js.native
  def cancel(): Unit = js.native
  def dispose(): Unit = js.native
}

@js.native
trait CancellationToken extends js.Object {
  def isCancellationRequested: Boolean = js.native
  def onCancellationRequested: IEvent[js.Any] = js.native
}

@js.native
@JSGlobal("monaco.Uri")
class Uri extends UriComponents {
  override def scheme: String = js.native
  override def authority: String = js.native
  override def path: String = js.native
  override def query: String = js.native
  override def fragment: String = js.native
  def fsPath: String = js.native
  def `with`(change: js.Any): Uri = js.native
  def toString(skipEncoding: Boolean = ???): String = js.native
  def toJSON(): js.Object = js.native
}

@js.native
@JSGlobal("monaco.Uri")
object Uri extends js.Object {
  def isUri(thing: js.Any): Boolean = js.native
  def parse(value: String): Uri = js.native
  def file(path: String): Uri = js.native
  def from(components: js.Any): Uri = js.native
  def revive(data: UriComponents | js.Any): Uri = js.native
}

@js.native
trait UriComponents extends js.Object {
  def scheme: String = js.native
  def authority: String = js.native
  def path: String = js.native
  def query: String = js.native
  def fragment: String = js.native
}

@js.native
sealed trait KeyCode extends js.Object {}

@js.native
@JSGlobal("monaco.KeyCode")
object KeyCode extends js.Object {
  var Unknown: KeyCode = js.native
  var Backspace: KeyCode = js.native
  var Tab: KeyCode = js.native
  var Enter: KeyCode = js.native
  var Shift: KeyCode = js.native
  var Ctrl: KeyCode = js.native
  var Alt: KeyCode = js.native
  var PauseBreak: KeyCode = js.native
  var CapsLock: KeyCode = js.native
  var Escape: KeyCode = js.native
  var Space: KeyCode = js.native
  var PageUp: KeyCode = js.native
  var PageDown: KeyCode = js.native
  var End: KeyCode = js.native
  var Home: KeyCode = js.native
  var LeftArrow: KeyCode = js.native
  var UpArrow: KeyCode = js.native
  var RightArrow: KeyCode = js.native
  var DownArrow: KeyCode = js.native
  var Insert: KeyCode = js.native
  var Delete: KeyCode = js.native
  var KEY_0: KeyCode = js.native
  var KEY_1: KeyCode = js.native
  var KEY_2: KeyCode = js.native
  var KEY_3: KeyCode = js.native
  var KEY_4: KeyCode = js.native
  var KEY_5: KeyCode = js.native
  var KEY_6: KeyCode = js.native
  var KEY_7: KeyCode = js.native
  var KEY_8: KeyCode = js.native
  var KEY_9: KeyCode = js.native
  var KEY_A: KeyCode = js.native
  var KEY_B: KeyCode = js.native
  var KEY_C: KeyCode = js.native
  var KEY_D: KeyCode = js.native
  var KEY_E: KeyCode = js.native
  var KEY_F: KeyCode = js.native
  var KEY_G: KeyCode = js.native
  var KEY_H: KeyCode = js.native
  var KEY_I: KeyCode = js.native
  var KEY_J: KeyCode = js.native
  var KEY_K: KeyCode = js.native
  var KEY_L: KeyCode = js.native
  var KEY_M: KeyCode = js.native
  var KEY_N: KeyCode = js.native
  var KEY_O: KeyCode = js.native
  var KEY_P: KeyCode = js.native
  var KEY_Q: KeyCode = js.native
  var KEY_R: KeyCode = js.native
  var KEY_S: KeyCode = js.native
  var KEY_T: KeyCode = js.native
  var KEY_U: KeyCode = js.native
  var KEY_V: KeyCode = js.native
  var KEY_W: KeyCode = js.native
  var KEY_X: KeyCode = js.native
  var KEY_Y: KeyCode = js.native
  var KEY_Z: KeyCode = js.native
  var Meta: KeyCode = js.native
  var ContextMenu: KeyCode = js.native
  var F1: KeyCode = js.native
  var F2: KeyCode = js.native
  var F3: KeyCode = js.native
  var F4: KeyCode = js.native
  var F5: KeyCode = js.native
  var F6: KeyCode = js.native
  var F7: KeyCode = js.native
  var F8: KeyCode = js.native
  var F9: KeyCode = js.native
  var F10: KeyCode = js.native
  var F11: KeyCode = js.native
  var F12: KeyCode = js.native
  var F13: KeyCode = js.native
  var F14: KeyCode = js.native
  var F15: KeyCode = js.native
  var F16: KeyCode = js.native
  var F17: KeyCode = js.native
  var F18: KeyCode = js.native
  var F19: KeyCode = js.native
  var NumLock: KeyCode = js.native
  var ScrollLock: KeyCode = js.native
  var US_SEMICOLON: KeyCode = js.native
  var US_EQUAL: KeyCode = js.native
  var US_COMMA: KeyCode = js.native
  var US_MINUS: KeyCode = js.native
  var US_DOT: KeyCode = js.native
  var US_SLASH: KeyCode = js.native
  var US_BACKTICK: KeyCode = js.native
  var US_OPEN_SQUARE_BRACKET: KeyCode = js.native
  var US_BACKSLASH: KeyCode = js.native
  var US_CLOSE_SQUARE_BRACKET: KeyCode = js.native
  var US_QUOTE: KeyCode = js.native
  var OEM_8: KeyCode = js.native
  var OEM_102: KeyCode = js.native
  var NUMPAD_0: KeyCode = js.native
  var NUMPAD_1: KeyCode = js.native
  var NUMPAD_2: KeyCode = js.native
  var NUMPAD_3: KeyCode = js.native
  var NUMPAD_4: KeyCode = js.native
  var NUMPAD_5: KeyCode = js.native
  var NUMPAD_6: KeyCode = js.native
  var NUMPAD_7: KeyCode = js.native
  var NUMPAD_8: KeyCode = js.native
  var NUMPAD_9: KeyCode = js.native
  var NUMPAD_MULTIPLY: KeyCode = js.native
  var NUMPAD_ADD: KeyCode = js.native
  var NUMPAD_SEPARATOR: KeyCode = js.native
  var NUMPAD_SUBTRACT: KeyCode = js.native
  var NUMPAD_DECIMAL: KeyCode = js.native
  var NUMPAD_DIVIDE: KeyCode = js.native
  var KEY_IN_COMPOSITION: KeyCode = js.native
  var ABNT_C1: KeyCode = js.native
  var ABNT_C2: KeyCode = js.native
  var MAX_VALUE: KeyCode = js.native
  @JSBracketAccess
  def apply(value: KeyCode): String = js.native
}

@js.native
@JSGlobal("monaco.KeyMod")
class KeyMod extends js.Object {}

@js.native
@JSGlobal("monaco.KeyMod")
object KeyMod extends js.Object {
  def CtrlCmd: Double = js.native
  def Shift: Double = js.native
  def Alt: Double = js.native
  def WinCtrl: Double = js.native
  def chord(firstPart: Double, secondPart: Double): Double = js.native
}

@js.native
trait IMarkdownString extends js.Object {
  var value: String = js.native
  var isTrusted: Boolean = js.native
}

@js.native
trait IKeyboardEvent extends js.Object {
  def browserEvent: KeyboardEvent = js.native
  def target: HTMLElement = js.native
  def ctrlKey: Boolean = js.native
  def shiftKey: Boolean = js.native
  def altKey: Boolean = js.native
  def metaKey: Boolean = js.native
  def keyCode: KeyCode = js.native
  def code: String = js.native
  def equals(keybinding: Double): Boolean = js.native
  def preventDefault(): Unit = js.native
  def stopPropagation(): Unit = js.native
}

@js.native
trait IMouseEvent extends js.Object {
  def browserEvent: MouseEvent = js.native
  def leftButton: Boolean = js.native
  def middleButton: Boolean = js.native
  def rightButton: Boolean = js.native
  def target: HTMLElement = js.native
  def detail: Double = js.native
  def posx: Double = js.native
  def posy: Double = js.native
  def ctrlKey: Boolean = js.native
  def shiftKey: Boolean = js.native
  def altKey: Boolean = js.native
  def metaKey: Boolean = js.native
  def timestamp: Double = js.native
  def preventDefault(): Unit = js.native
  def stopPropagation(): Unit = js.native
}

@js.native
trait IScrollEvent extends js.Object {
  def scrollTop: Double = js.native
  def scrollLeft: Double = js.native
  def scrollWidth: Double = js.native
  def scrollHeight: Double = js.native
  def scrollTopChanged: Boolean = js.native
  def scrollLeftChanged: Boolean = js.native
  def scrollWidthChanged: Boolean = js.native
  def scrollHeightChanged: Boolean = js.native
}

@js.native
trait IPosition extends js.Object {
  def lineNumber: Double = js.native
  def column: Double = js.native
}

@js.native
@JSGlobal("monaco.Position")
class Position protected () extends IPosition {
  def this(lineNumber: Double, column: Double) = this()
  override def lineNumber: Double = js.native
  override def column: Double = js.native
  def equals(other: IPosition): Boolean = js.native
  def isBefore(other: IPosition): Boolean = js.native
  def isBeforeOrEqual(other: IPosition): Boolean = js.native
  override def clone(): Position = js.native
  override def toString(): String = js.native
}

@js.native
@JSGlobal("monaco.Position")
object Position extends js.Object {
  def equals(a: IPosition, b: IPosition): Boolean = js.native
  def isBefore(a: IPosition, b: IPosition): Boolean = js.native
  def isBeforeOrEqual(a: IPosition, b: IPosition): Boolean = js.native
  def compare(a: IPosition, b: IPosition): Double = js.native
  def lift(pos: IPosition): Position = js.native
  def isIPosition(obj: js.Any): Boolean = js.native
}

@js.native
trait IRange extends js.Object {
  def startLineNumber: Double = js.native
  def startColumn: Double = js.native
  def endLineNumber: Double = js.native
  def endColumn: Double = js.native
}

@js.native
@JSGlobal("monaco.Range")
class Range protected () extends IRange {
  def this(
      startLineNumber: Double,
      startColumn: Double,
      endLineNumber: Double,
      endColumn: Double
  ) = this()
  override def startLineNumber: Double = js.native
  override def startColumn: Double = js.native
  override def endLineNumber: Double = js.native
  override def endColumn: Double = js.native
  def isEmpty(): Boolean = js.native
  def containsPosition(position: IPosition): Boolean = js.native
  def containsRange(range: IRange): Boolean = js.native
  def plusRange(range: IRange): Range = js.native
  def intersectRanges(range: IRange): Range = js.native
  def equalsRange(other: IRange): Boolean = js.native
  def getEndPosition(): Position = js.native
  def getStartPosition(): Position = js.native
  override def toString(): String = js.native
  def setEndPosition(
      endLineNumber: Double,
      endColumn: Double
  ): Range = js.native
  def setStartPosition(
      startLineNumber: Double,
      startColumn: Double
  ): Range = js.native
  def collapseToStart(): Range = js.native
}

@js.native
@JSGlobal("monaco.Range")
object Range extends js.Object {
  def isEmpty(range: IRange): Boolean = js.native
  def containsPosition(range: IRange, position: IPosition): Boolean = js.native
  def containsRange(range: IRange, otherRange: IRange): Boolean = js.native
  def plusRange(a: IRange, b: IRange): Range = js.native
  def intersectRanges(a: IRange, b: IRange): Range = js.native
  def equalsRange(a: IRange, b: IRange): Boolean = js.native
  def collapseToStart(range: IRange): Range = js.native
  def fromPositions(start: IPosition, end: IPosition = ???): Range = js.native
  def lift(range: IRange): Range = js.native
  def isIRange(obj: js.Any): Boolean = js.native
  def areIntersectingOrTouching(a: IRange, b: IRange): Boolean = js.native
  def compareRangesUsingStarts(a: IRange, b: IRange): Double = js.native
  def compareRangesUsingEnds(a: IRange, b: IRange): Double = js.native
  def spansMultipleLines(range: IRange): Boolean = js.native
}

@js.native
trait ISelection extends js.Object {
  def selectionStartLineNumber: Double = js.native
  def selectionStartColumn: Double = js.native
  def positionLineNumber: Double = js.native
  def positionColumn: Double = js.native
}

@js.native
@JSGlobal("monaco.Selection")
class Selection protected () extends Range {
  def this(
      selectionStartLineNumber: Double,
      selectionStartColumn: Double,
      positionLineNumber: Double,
      positionColumn: Double
  ) = this()
  def selectionStartLineNumber: Double = js.native
  def selectionStartColumn: Double = js.native
  def positionLineNumber: Double = js.native
  def positionColumn: Double = js.native
  override def clone(): Selection = js.native
  override def toString(): String = js.native
  def equalsSelection(other: ISelection): Boolean = js.native
  def getDirection(): SelectionDirection = js.native
  override def setEndPosition(
      endLineNumber: Double,
      endColumn: Double
  ): Selection = js.native
  def getPosition(): Position = js.native
  override def setStartPosition(
      startLineNumber: Double,
      startColumn: Double
  ): Selection = js.native
}

@js.native
@JSGlobal("monaco.Selection")
object Selection extends js.Object {
  def selectionsEqual(a: ISelection, b: ISelection): Boolean = js.native
  def fromPositions(
      start: IPosition,
      end: IPosition = ???
  ): Selection = js.native
  def liftSelection(sel: ISelection): Selection = js.native
  def selectionsArrEqual(
      a: js.Array[ISelection],
      b: js.Array[ISelection]
  ): Boolean = js.native
  def isISelection(obj: js.Any): Boolean = js.native
  def createWithDirection(
      startLineNumber: Double,
      startColumn: Double,
      endLineNumber: Double,
      endColumn: Double,
      direction: SelectionDirection
  ): Selection = js.native
}

@js.native
sealed trait SelectionDirection extends js.Object {}

@js.native
@JSGlobal("monaco.SelectionDirection")
object SelectionDirection extends js.Object {
  var LTR: SelectionDirection = js.native
  var RTL: SelectionDirection = js.native
  @JSBracketAccess
  def apply(value: SelectionDirection): String = js.native
}

@js.native
@JSGlobal("monaco.Token")
class Token protected () extends js.Object {
  def this(offset: Double, `type`: String, language: String) = this()
  var _tokenBrand: Unit = js.native
  def offset: Double = js.native
  def `type`: String = js.native
  def language: String = js.native
  override def toString(): String = js.native
}

package editor {

  @js.native
  trait IDiffNavigator extends js.Object {
    def canNavigate(): Boolean = js.native
    def next(): Unit = js.native
    def previous(): Unit = js.native
    def dispose(): Unit = js.native
  }

  @js.native
  trait IDiffNavigatorOptions extends js.Object {
    def followsCaret: Boolean = js.native
    def ignoreCharChanges: Boolean = js.native
    def alwaysRevealFirst: Boolean = js.native
  }

  @js.native
  trait IStandaloneThemeData extends js.Object {
    var base: BuiltinTheme = js.native
    var inherit: Boolean = js.native
    var rules: js.Array[ITokenThemeRule] = js.native
    var colors: IColors = js.native
  }

  @js.native
  trait ITokenThemeRule extends js.Object {
    var token: String = js.native
    var foreground: String = js.native
    var background: String = js.native
    var fontStyle: String = js.native
  }

  @js.native
  trait MonacoWebWorker[T] extends js.Object {
    def dispose(): Unit = js.native
    def getProxy(): Promise[T] = js.native
    def withSyncedResources(resources: js.Array[Uri]): Promise[T] = js.native
  }

  @js.native
  trait IWebWorkerOptions extends js.Object {
    var moduleId: String = js.native
    var createData: js.Any = js.native
    var label: String = js.native
  }

  // @js.native - Non-native to allow extending it in Scala
  trait IActionDescriptor extends js.Object {
    var id: String
    var label: String
    var precondition: String
    var keybindings: js.Array[Double]
    var keybindingContext: String
    var contextMenuGroupId: String
    var contextMenuOrder: Double
    def run(editor: ICodeEditor): Promise[Unit]
  }

  @js.native
  trait IEditorConstructionOptions extends IEditorOptions {
    var model: ITextModel = js.native
    var value: String = js.native
    var language: String = js.native
    var theme: String = js.native
    var accessibilityHelpUrl: String = js.native
  }

  @js.native
  trait IDiffEditorConstructionOptions extends IDiffEditorOptions {
    var theme: String = js.native
  }

  @js.native
  trait IStandaloneCodeEditor extends ICodeEditor {
    def addCommand(
        keybinding: Double,
        handler: ICommandHandler,
        context: String
    ): String = js.native
    def createContextKey[T](
        key: String,
        defaultValue: T
    ): IContextKey[T] = js.native
    def addAction(descriptor: IActionDescriptor): IDisposable = js.native
  }

  @js.native
  trait IStandaloneDiffEditor extends IDiffEditor {
    def addCommand(
        keybinding: Double,
        handler: ICommandHandler,
        context: String
    ): String = js.native
    def createContextKey[T](
        key: String,
        defaultValue: T
    ): IContextKey[T] = js.native
    def addAction(descriptor: IActionDescriptor): IDisposable = js.native
    override def getOriginalEditor(): IStandaloneCodeEditor = js.native
    override def getModifiedEditor(): IStandaloneCodeEditor = js.native
  }

  @js.native
  trait ICommandHandler extends js.Object {
    def apply(args: js.Any*): Unit = js.native
  }

  @js.native
  trait IContextKey[T] extends js.Object {
    def set(value: T): Unit = js.native
    def reset(): Unit = js.native
    def get(): T = js.native
  }

  @js.native
  trait IEditorOverrideServices extends js.Object {
    @JSBracketAccess
    def apply(index: String): js.Any = js.native
    @JSBracketAccess
    def update(index: String, v: js.Any): Unit = js.native
  }

  @js.native
  trait IMarker extends js.Object {
    var owner: String = js.native
    var resource: Uri = js.native
    var severity: MarkerSeverity = js.native
    var code: String = js.native
    var message: String = js.native
    var source: String = js.native
    var startLineNumber: Double = js.native
    var startColumn: Double = js.native
    var endLineNumber: Double = js.native
    var endColumn: Double = js.native
    var relatedInformation: js.Array[IRelatedInformation] = js.native
  }

  @js.native
  trait IMarkerData extends js.Object {
    var code: String = js.native
    var severity: MarkerSeverity = js.native
    var message: String = js.native
    var source: String = js.native
    var startLineNumber: Double = js.native
    var startColumn: Double = js.native
    var endLineNumber: Double = js.native
    var endColumn: Double = js.native
    var relatedInformation: js.Array[IRelatedInformation] = js.native
  }

  @js.native
  trait IRelatedInformation extends js.Object {
    var resource: Uri = js.native
    var message: String = js.native
    var startLineNumber: Double = js.native
    var startColumn: Double = js.native
    var endLineNumber: Double = js.native
    var endColumn: Double = js.native
  }

  @js.native
  trait IColorizerOptions extends js.Object {
    var tabSize: Double = js.native
  }

  @js.native
  trait IColorizerElementOptions extends IColorizerOptions {
    var theme: String = js.native
    var mimeType: String = js.native
  }

  @js.native
  sealed trait ScrollbarVisibility extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.ScrollbarVisibility")
  object ScrollbarVisibility extends js.Object {
    var Auto: ScrollbarVisibility = js.native
    var Hidden: ScrollbarVisibility = js.native
    var Visible: ScrollbarVisibility = js.native
    @JSBracketAccess
    def apply(value: ScrollbarVisibility): String = js.native
  }

  @js.native
  trait ThemeColor extends js.Object {
    var id: String = js.native
  }

  @js.native
  sealed trait OverviewRulerLane extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.OverviewRulerLane")
  object OverviewRulerLane extends js.Object {
    var Left: OverviewRulerLane = js.native
    var Center: OverviewRulerLane = js.native
    var Right: OverviewRulerLane = js.native
    var Full: OverviewRulerLane = js.native
    @JSBracketAccess
    def apply(value: OverviewRulerLane): String = js.native
  }

  @js.native
  trait IModelDecorationOverviewRulerOptions extends js.Object {
    var color: String | ThemeColor = js.native
    var darkColor: String | ThemeColor = js.native
    var hcColor: String | ThemeColor = js.native
    var position: OverviewRulerLane = js.native
  }

  @js.native
  trait IModelDecorationOptions extends js.Object {
    var stickiness: TrackedRangeStickiness = js.native
    var className: String = js.native
    var glyphMarginHoverMessage: IMarkdownString | js.Array[
      IMarkdownString
    ] = js.native
    var hoverMessage: IMarkdownString | js.Array[IMarkdownString] = js.native
    var isWholeLine: Boolean = js.native
    var zIndex: Double = js.native
    var overviewRuler: IModelDecorationOverviewRulerOptions = js.native
    var glyphMarginClassName: String = js.native
    var linesDecorationsClassName: String = js.native
    var marginClassName: String = js.native
    var inlineClassName: String = js.native
    var inlineClassNameAffectsLetterSpacing: Boolean = js.native
    var beforeContentClassName: String = js.native
    var afterContentClassName: String = js.native
  }

  @js.native
  trait IModelDeltaDecoration extends js.Object {
    var range: IRange = js.native
    var options: IModelDecorationOptions = js.native
  }

  @js.native
  trait IModelDecoration extends js.Object {
    def id: String = js.native
    def ownerId: Double = js.native
    def range: Range = js.native
    def options: IModelDecorationOptions = js.native
  }

  @js.native
  trait IWordAtPosition extends js.Object {
    def word: String = js.native
    def startColumn: Double = js.native
    def endColumn: Double = js.native
  }

  @js.native
  sealed trait EndOfLinePreference extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.EndOfLinePreference")
  object EndOfLinePreference extends js.Object {
    var TextDefined: EndOfLinePreference = js.native
    var LF: EndOfLinePreference = js.native
    var CRLF: EndOfLinePreference = js.native
    @JSBracketAccess
    def apply(value: EndOfLinePreference): String = js.native
  }

  @js.native
  sealed trait DefaultEndOfLine extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.DefaultEndOfLine")
  object DefaultEndOfLine extends js.Object {
    var LF: DefaultEndOfLine = js.native
    var CRLF: DefaultEndOfLine = js.native
    @JSBracketAccess
    def apply(value: DefaultEndOfLine): String = js.native
  }

  @js.native
  sealed trait EndOfLineSequence extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.EndOfLineSequence")
  object EndOfLineSequence extends js.Object {
    var LF: EndOfLineSequence = js.native
    var CRLF: EndOfLineSequence = js.native
    @JSBracketAccess
    def apply(value: EndOfLineSequence): String = js.native
  }

  @js.native
  trait ISingleEditOperationIdentifier extends js.Object {
    var major: Double = js.native
    var minor: Double = js.native
  }

  @js.native
  trait ISingleEditOperation extends js.Object {
    var range: IRange = js.native
    var text: String = js.native
    var forceMoveMarkers: Boolean = js.native
  }

  @js.native
  trait IIdentifiedSingleEditOperation extends js.Object {
    var range: Range = js.native
    var text: String = js.native
    var forceMoveMarkers: Boolean = js.native
  }

  @js.native
  trait ICursorStateComputer extends js.Object {
    def apply(
        inverseEditOperations: js.Array[IIdentifiedSingleEditOperation]
    ): js.Array[Selection] = js.native
  }

  @js.native
  @JSGlobal("monaco.editor.TextModelResolvedOptions")
  class TextModelResolvedOptions extends js.Object {
    var _textModelResolvedOptionsBrand: Unit = js.native
    def tabSize: Double = js.native
    def insertSpaces: Boolean = js.native
    def defaultEOL: DefaultEndOfLine = js.native
    def trimAutoWhitespace: Boolean = js.native
  }

  @js.native
  trait ITextModelUpdateOptions extends js.Object {
    var tabSize: Double = js.native
    var insertSpaces: Boolean = js.native
    var trimAutoWhitespace: Boolean = js.native
  }

  @js.native
  @JSGlobal("monaco.editor.FindMatch")
  class FindMatch extends js.Object {
    var _findMatchBrand: Unit = js.native
    def range: Range = js.native
    def matches: js.Array[String] = js.native
  }

  @js.native
  sealed trait TrackedRangeStickiness extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.TrackedRangeStickiness")
  object TrackedRangeStickiness extends js.Object {
    var AlwaysGrowsWhenTypingAtEdges: TrackedRangeStickiness = js.native
    var NeverGrowsWhenTypingAtEdges: TrackedRangeStickiness = js.native
    var GrowsOnlyWhenTypingBefore: TrackedRangeStickiness = js.native
    var GrowsOnlyWhenTypingAfter: TrackedRangeStickiness = js.native
    @JSBracketAccess
    def apply(value: TrackedRangeStickiness): String = js.native
  }

  @js.native
  trait ITextModel extends js.Object {
    def uri: Uri = js.native
    def id: String = js.native
    def getOptions(): TextModelResolvedOptions = js.native
    def getVersionId(): Double = js.native
    def getAlternativeVersionId(): Double = js.native
    def setValue(newValue: String): Unit = js.native
    def getValue(
        eol: EndOfLinePreference = ???,
        preserveBOM: Boolean = ???
    ): String = js.native
    def getValueLength(
        eol: EndOfLinePreference = ???,
        preserveBOM: Boolean = ???
    ): Double = js.native
    def getValueInRange(
        range: IRange,
        eol: EndOfLinePreference = ???
    ): String = js.native
    def getValueLengthInRange(range: IRange): Double = js.native
    def getLineCount(): Double = js.native
    def getLineContent(lineNumber: Double): String = js.native
    def getLineLength(lineNumber: Double): Double = js.native
    def getLinesContent(): js.Array[String] = js.native
    def getEOL(): String = js.native
    def setEOL(eol: EndOfLineSequence): Unit = js.native
    def getLineMinColumn(lineNumber: Double): Double = js.native
    def getLineMaxColumn(lineNumber: Double): Double = js.native
    def getLineFirstNonWhitespaceColumn(lineNumber: Double): Double = js.native
    def getLineLastNonWhitespaceColumn(lineNumber: Double): Double = js.native
    def validatePosition(position: IPosition): Position = js.native
    def modifyPosition(
        position: IPosition,
        offset: Double
    ): Position = js.native
    def validateRange(range: IRange): Range = js.native
    def getOffsetAt(position: IPosition): Double = js.native
    def getPositionAt(offset: Double): Position = js.native
    def getFullModelRange(): Range = js.native
    def isDisposed(): Boolean = js.native
    def findMatches(
        searchString: String,
        searchOnlyEditableRange: Boolean,
        isRegex: Boolean,
        matchCase: Boolean,
        wordSeparators: String | Null,
        captureMatches: Boolean,
        limitResultCount: Double = ???
    ): js.Array[FindMatch] = js.native
    def findMatches(
        searchString: String,
        searchScope: IRange,
        isRegex: Boolean,
        matchCase: Boolean,
        wordSeparators: String | Null,
        captureMatches: Boolean,
        limitResultCount: Double // = ??? - compile error after erasure
    ): js.Array[FindMatch] = js.native
    def findNextMatch(
        searchString: String,
        searchStart: IPosition,
        isRegex: Boolean,
        matchCase: Boolean,
        wordSeparators: String | Null,
        captureMatches: Boolean
    ): FindMatch = js.native
    def findPreviousMatch(
        searchString: String,
        searchStart: IPosition,
        isRegex: Boolean,
        matchCase: Boolean,
        wordSeparators: String | Null,
        captureMatches: Boolean
    ): FindMatch = js.native
    def getModeId(): String = js.native
    def getWordAtPosition(position: IPosition): IWordAtPosition = js.native
    def getWordUntilPosition(position: IPosition): IWordAtPosition = js.native
    def deltaDecorations(
        oldDecorations: js.Array[String],
        newDecorations: js.Array[IModelDeltaDecoration],
        ownerId: Double = ???
    ): js.Array[String] = js.native
    def getDecorationOptions(id: String): IModelDecorationOptions = js.native
    def getDecorationRange(id: String): Range = js.native
    def getLineDecorations(
        lineNumber: Double,
        ownerId: Double = ???,
        filterOutValidation: Boolean = ???
    ): js.Array[IModelDecoration] = js.native
    def getLinesDecorations(
        startLineNumber: Double,
        endLineNumber: Double,
        ownerId: Double = ???,
        filterOutValidation: Boolean = ???
    ): js.Array[IModelDecoration] = js.native
    def getDecorationsInRange(
        range: IRange,
        ownerId: Double = ???,
        filterOutValidation: Boolean = ???
    ): js.Array[IModelDecoration] = js.native
    def getAllDecorations(
        ownerId: Double = ???,
        filterOutValidation: Boolean = ???
    ): js.Array[IModelDecoration] = js.native
    def getOverviewRulerDecorations(
        ownerId: Double = ???,
        filterOutValidation: Boolean = ???
    ): js.Array[IModelDecoration] = js.native
    def normalizeIndentation(str: String): String = js.native
    def getOneIndent(): String = js.native
    def updateOptions(newOpts: ITextModelUpdateOptions): Unit = js.native
    def detectIndentation(
        defaultInsertSpaces: Boolean,
        defaultTabSize: Double
    ): Unit = js.native
    def pushStackElement(): Unit = js.native
    def pushEditOperations(
        beforeCursorState: js.Array[Selection],
        editOperations: js.Array[IIdentifiedSingleEditOperation],
        cursorStateComputer: ICursorStateComputer
    ): js.Array[Selection] = js.native
    def applyEdits(
        operations: js.Array[IIdentifiedSingleEditOperation]
    ): js.Array[IIdentifiedSingleEditOperation] = js.native
    def onDidChangeContent(
        listener: js.Function1[IModelContentChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeDecorations(
        listener: js.Function1[IModelDecorationsChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeOptions(
        listener: js.Function1[IModelOptionsChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeLanguage(
        listener: js.Function1[IModelLanguageChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeLanguageConfiguration(
        listener: js.Function1[IModelLanguageConfigurationChangedEvent, Unit]
    ): IDisposable = js.native
    def onWillDispose(listener: js.Function0[Unit]): IDisposable = js.native
    def dispose(): Unit = js.native
  }

  @js.native
  trait IEditOperationBuilder extends js.Object {
    def addEditOperation(range: Range, text: String): Unit = js.native
    def addTrackedEditOperation(range: Range, text: String): Unit = js.native
    def trackSelection(
        selection: Selection,
        trackPreviousOnEmpty: Boolean = ???
    ): String = js.native
  }

  @js.native
  trait ICursorStateComputerData extends js.Object {
    def getInverseEditOperations(): js.Array[
      IIdentifiedSingleEditOperation
    ] = js.native
    def getTrackedSelection(id: String): Selection = js.native
  }

  @js.native
  trait ICommand extends js.Object {
    def getEditOperations(
        model: ITextModel,
        builder: IEditOperationBuilder
    ): Unit = js.native
    def computeCursorState(
        model: ITextModel,
        helper: ICursorStateComputerData
    ): Selection = js.native
  }

  @js.native
  trait IDiffEditorModel extends js.Object {
    var original: ITextModel = js.native
    var modified: ITextModel = js.native
  }

  @js.native
  trait IModelChangedEvent extends js.Object {
    def oldModelUrl: Uri = js.native
    def newModelUrl: Uri = js.native
  }

  @js.native
  trait IDimension extends js.Object {
    var width: Double = js.native
    var height: Double = js.native
  }

  @js.native
  trait IChange extends js.Object {
    def originalStartLineNumber: Double = js.native
    def originalEndLineNumber: Double = js.native
    def modifiedStartLineNumber: Double = js.native
    def modifiedEndLineNumber: Double = js.native
  }

  @js.native
  trait ICharChange extends IChange {
    def originalStartColumn: Double = js.native
    def originalEndColumn: Double = js.native
    def modifiedStartColumn: Double = js.native
    def modifiedEndColumn: Double = js.native
  }

  @js.native
  trait ILineChange extends IChange {
    def charChanges: js.Array[ICharChange] = js.native
  }

  @js.native
  trait INewScrollPosition extends js.Object {
    var scrollLeft: Double = js.native
    var scrollTop: Double = js.native
  }

  @js.native
  trait IEditorAction extends js.Object {
    def id: String = js.native
    def label: String = js.native
    def alias: String = js.native
    def isSupported(): Boolean = js.native
    def run(): Promise[Unit] = js.native
  }

  @js.native
  trait ICursorState extends js.Object {
    var inSelectionMode: Boolean = js.native
    var selectionStart: IPosition = js.native
    var position: IPosition = js.native
  }

  @js.native
  trait IViewState extends js.Object {
    var scrollTop: Double = js.native
    var scrollTopWithoutViewZones: Double = js.native
    var scrollLeft: Double = js.native
    var firstPosition: IPosition = js.native
    var firstPositionDeltaTop: Double = js.native
  }

  @js.native
  trait ICodeEditorViewState extends js.Object {
    var cursorState: js.Array[ICursorState] = js.native
    var viewState: IViewState = js.native
    var contributionsState: ICodeEditorViewState.ContributionsState = js.native
  }

  object ICodeEditorViewState {

    @js.native
    trait ContributionsState extends js.Object {
      @JSBracketAccess
      def apply(id: String): js.Any = js.native
      @JSBracketAccess
      def update(id: String, v: js.Any): Unit = js.native
    }
  }

  @js.native
  trait IDiffEditorViewState extends js.Object {
    var original: ICodeEditorViewState = js.native
    var modified: ICodeEditorViewState = js.native
  }

  @js.native
  sealed trait ScrollType extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.ScrollType")
  object ScrollType extends js.Object {
    var Smooth: ScrollType = js.native
    var Immediate: ScrollType = js.native
    @JSBracketAccess
    def apply(value: ScrollType): String = js.native
  }

  @js.native
  trait IEditor extends js.Object {
    def onDidDispose(listener: js.Function0[Unit]): IDisposable = js.native
    def dispose(): Unit = js.native
    def getId(): String = js.native
    def getEditorType(): String = js.native
    def updateOptions(newOptions: IEditorOptions): Unit = js.native
    def layout(dimension: IDimension = ???): Unit = js.native
    def focus(): Unit = js.native
    def isFocused(): Boolean = js.native
    def getSupportedActions(): js.Array[IEditorAction] = js.native
    def saveViewState(): IEditorViewState = js.native
    def restoreViewState(state: IEditorViewState): Unit = js.native
    def getVisibleColumnFromPosition(position: IPosition): Double = js.native
    def getPosition(): Position = js.native
    def setPosition(position: IPosition): Unit = js.native
    def revealLine(
        lineNumber: Double,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealLineInCenter(
        lineNumber: Double,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealLineInCenterIfOutsideViewport(
        lineNumber: Double,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealPosition(
        position: IPosition,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealPositionInCenter(
        position: IPosition,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealPositionInCenterIfOutsideViewport(
        position: IPosition,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def getSelection(): Selection = js.native
    def getSelections(): js.Array[Selection] = js.native
    def setSelection(selection: IRange): Unit = js.native
    def setSelection(selection: Range): Unit = js.native
    def setSelection(selection: ISelection): Unit = js.native
    def setSelection(selection: Selection): Unit = js.native
    def setSelections(selections: js.Array[ISelection]): Unit = js.native
    def revealLines(
        startLineNumber: Double,
        endLineNumber: Double,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealLinesInCenter(
        lineNumber: Double,
        endLineNumber: Double,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealLinesInCenterIfOutsideViewport(
        lineNumber: Double,
        endLineNumber: Double,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealRange(
        range: IRange,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealRangeInCenter(
        range: IRange,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealRangeAtTop(
        range: IRange,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def revealRangeInCenterIfOutsideViewport(
        range: IRange,
        scrollType: ScrollType = ???
    ): Unit = js.native
    def trigger(
        source: String,
        handlerId: String,
        payload: js.Any
    ): Unit = js.native
    def getModel(): IEditorModel = js.native
    def setModel(model: IEditorModel): Unit = js.native
  }

  @js.native
  trait IEditorContribution extends js.Object {
    def getId(): String = js.native
    def dispose(): Unit = js.native
    def saveViewState(): js.Dynamic = js.native
    def restoreViewState(state: js.Any): Unit = js.native
  }

  @js.native
  @JSGlobal("monaco.editor.EditorType")
  object EditorType extends js.Object {
    var ICodeEditor: String = js.native
    var IDiffEditor: String = js.native
  }

  @js.native
  trait IModelLanguageChangedEvent extends js.Object {
    def oldLanguage: String = js.native
    def newLanguage: String = js.native
  }

  @js.native
  trait IModelLanguageConfigurationChangedEvent extends js.Object {}

  @js.native
  trait IModelContentChange extends js.Object {
    def range: IRange = js.native
    def rangeOffset: Double = js.native
    def rangeLength: Double = js.native
    def text: String = js.native
  }

  @js.native
  trait IModelContentChangedEvent extends js.Object {
    def changes: js.Array[IModelContentChange] = js.native
    def eol: String = js.native
    def versionId: Double = js.native
    def isUndoing: Boolean = js.native
    def isRedoing: Boolean = js.native
    def isFlush: Boolean = js.native
  }

  @js.native
  trait IModelDecorationsChangedEvent extends js.Object {}

  @js.native
  trait IModelTokensChangedEvent extends js.Object {
    def ranges: js.Array[js.Any] = js.native
  }

  @js.native
  trait IModelOptionsChangedEvent extends js.Object {
    def tabSize: Boolean = js.native
    def insertSpaces: Boolean = js.native
    def trimAutoWhitespace: Boolean = js.native
  }

  @js.native
  sealed trait CursorChangeReason extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.CursorChangeReason")
  object CursorChangeReason extends js.Object {
    var NotSet: CursorChangeReason = js.native
    var ContentFlush: CursorChangeReason = js.native
    var RecoverFromMarkers: CursorChangeReason = js.native
    var Explicit: CursorChangeReason = js.native
    var Paste: CursorChangeReason = js.native
    var Undo: CursorChangeReason = js.native
    var Redo: CursorChangeReason = js.native
    @JSBracketAccess
    def apply(value: CursorChangeReason): String = js.native
  }

  @js.native
  trait ICursorPositionChangedEvent extends js.Object {
    def position: Position = js.native
    def secondaryPositions: js.Array[Position] = js.native
    def reason: CursorChangeReason = js.native
    def source: String = js.native
  }

  @js.native
  trait ICursorSelectionChangedEvent extends js.Object {
    def selection: Selection = js.native
    def secondarySelections: js.Array[Selection] = js.native
    def source: String = js.native
    def reason: CursorChangeReason = js.native
  }

  @js.native
  trait IEditorScrollbarOptions extends js.Object {
    var arrowSize: Double = js.native
    var vertical: String = js.native
    var horizontal: String = js.native
    var useShadows: Boolean = js.native
    var verticalHasArrows: Boolean = js.native
    var horizontalHasArrows: Boolean = js.native
    var handleMouseWheel: Boolean = js.native
    var horizontalScrollbarSize: Double = js.native
    var verticalScrollbarSize: Double = js.native
    var verticalSliderSize: Double = js.native
    var horizontalSliderSize: Double = js.native
  }

  @js.native
  trait IEditorFindOptions extends js.Object {
    var seedSearchStringFromSelection: Boolean = js.native
    var autoFindInSelection: Boolean = js.native
  }

  @js.native
  trait IEditorMinimapOptions extends js.Object {
    var enabled: Boolean = js.native
    var side: String = js.native
    var showSlider: String = js.native
    var renderCharacters: Boolean = js.native
    var maxColumn: Double = js.native
  }

  @js.native
  trait IEditorLightbulbOptions extends js.Object {
    var enabled: Boolean = js.native
  }

  @js.native
  trait ICodeActionsOnSaveOptions extends js.Object {
    @JSBracketAccess
    def apply(kind: String): Boolean = js.native
    @JSBracketAccess
    def update(kind: String, v: Boolean): Unit = js.native
  }

  @js.native
  trait IEditorOptions extends js.Object {
    var ariaLabel: String = js.native
    var rulers: js.Array[Double] = js.native
    var wordSeparators: String = js.native
    var selectionClipboard: Boolean = js.native
    var lineNumbers: String | js.Function1[Double, String] = js.native
    var selectOnLineNumbers: Boolean = js.native
    var lineNumbersMinChars: Double = js.native
    var glyphMargin: Boolean = js.native
    var lineDecorationsWidth: Double | String = js.native
    var revealHorizontalRightPadding: Double = js.native
    var roundedSelection: Boolean = js.native
    var extraEditorClassName: String = js.native
    var readOnly: Boolean = js.native
    var scrollbar: IEditorScrollbarOptions = js.native
    var minimap: IEditorMinimapOptions = js.native
    var find: IEditorFindOptions = js.native
    var fixedOverflowWidgets: Boolean = js.native
    var overviewRulerLanes: Double = js.native
    var overviewRulerBorder: Boolean = js.native
    var cursorBlinking: String = js.native
    var mouseWheelZoom: Boolean = js.native
    var cursorStyle: String = js.native
    var cursorWidth: Double = js.native
    var fontLigatures: Boolean = js.native
    var disableLayerHinting: Boolean = js.native
    var disableMonospaceOptimizations: Boolean = js.native
    var hideCursorInOverviewRuler: Boolean = js.native
    var scrollBeyondLastLine: Boolean = js.native
    var smoothScrolling: Boolean = js.native
    var automaticLayout: Boolean = js.native
    var wordWrap: String = js.native
    var wordWrapColumn: Double = js.native
    var wordWrapMinified: Boolean = js.native
    var wrappingIndent: String = js.native
    var wordWrapBreakBeforeCharacters: String = js.native
    var wordWrapBreakAfterCharacters: String = js.native
    var wordWrapBreakObtrusiveCharacters: String = js.native
    var stopRenderingLineAfter: Double = js.native
    var hover: Boolean = js.native
    var links: Boolean = js.native
    var colorDecorators: Boolean = js.native
    var contextmenu: Boolean = js.native
    var mouseWheelScrollSensitivity: Double = js.native
    var multiCursorModifier: String = js.native
    var multiCursorMergeOverlapping: Boolean = js.native
    var accessibilitySupport: String = js.native
    var quickSuggestions: Boolean | js.Any = js.native
    var quickSuggestionsDelay: Double = js.native
    var parameterHints: Boolean = js.native
    var iconsInSuggestions: Boolean = js.native
    var autoClosingBrackets: Boolean = js.native
    var autoIndent: Boolean = js.native
    var formatOnType: Boolean = js.native
    var formatOnPaste: Boolean = js.native
    var dragAndDrop: Boolean = js.native
    var suggestOnTriggerCharacters: Boolean = js.native
    var acceptSuggestionOnEnter: Boolean | String = js.native
    var acceptSuggestionOnCommitCharacter: Boolean = js.native
    var snippetSuggestions: String = js.native
    var emptySelectionClipboard: Boolean = js.native
    var wordBasedSuggestions: Boolean = js.native
    var suggestSelection: String = js.native
    var suggestFontSize: Double = js.native
    var suggestLineHeight: Double = js.native
    var selectionHighlight: Boolean = js.native
    var occurrencesHighlight: Boolean = js.native
    var codeLens: Boolean = js.native
    var lightbulb: IEditorLightbulbOptions = js.native
    var codeActionsOnSave: ICodeActionsOnSaveOptions = js.native
    var codeActionsOnSaveTimeout: Double = js.native
    var folding: Boolean = js.native
    var foldingStrategy: String = js.native
    var showFoldingControls: String = js.native
    var matchBrackets: Boolean = js.native
    var renderWhitespace: String = js.native
    var renderControlCharacters: Boolean = js.native
    var renderIndentGuides: Boolean = js.native
    var renderLineHighlight: String = js.native
    var useTabStops: Boolean = js.native
    var fontFamily: String = js.native
    var fontWeight: String = js.native
    var fontSize: Double = js.native
    var lineHeight: Double = js.native
    var letterSpacing: Double = js.native
  }

  @js.native
  trait IDiffEditorOptions extends IEditorOptions {
    var enableSplitViewResizing: Boolean = js.native
    var renderSideBySide: Boolean = js.native
    var ignoreTrimWhitespace: Boolean = js.native
    var renderIndicators: Boolean = js.native
    var originalEditable: Boolean = js.native
  }

  @js.native
  sealed trait RenderMinimap extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.RenderMinimap")
  object RenderMinimap extends js.Object {
    var None: RenderMinimap = js.native
    var Small: RenderMinimap = js.native
    var Large: RenderMinimap = js.native
    var SmallBlocks: RenderMinimap = js.native
    var LargeBlocks: RenderMinimap = js.native
    @JSBracketAccess
    def apply(value: RenderMinimap): String = js.native
  }

  @js.native
  sealed trait WrappingIndent extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.WrappingIndent")
  object WrappingIndent extends js.Object {
    var None: WrappingIndent = js.native
    var Same: WrappingIndent = js.native
    var Indent: WrappingIndent = js.native
    @JSBracketAccess
    def apply(value: WrappingIndent): String = js.native
  }

  @js.native
  sealed trait TextEditorCursorBlinkingStyle extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.TextEditorCursorBlinkingStyle")
  object TextEditorCursorBlinkingStyle extends js.Object {
    var Hidden: TextEditorCursorBlinkingStyle = js.native
    var Blink: TextEditorCursorBlinkingStyle = js.native
    var Smooth: TextEditorCursorBlinkingStyle = js.native
    var Phase: TextEditorCursorBlinkingStyle = js.native
    var Expand: TextEditorCursorBlinkingStyle = js.native
    var Solid: TextEditorCursorBlinkingStyle = js.native
    @JSBracketAccess
    def apply(value: TextEditorCursorBlinkingStyle): String = js.native
  }

  @js.native
  sealed trait TextEditorCursorStyle extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.TextEditorCursorStyle")
  object TextEditorCursorStyle extends js.Object {
    var Line: TextEditorCursorStyle = js.native
    var Block: TextEditorCursorStyle = js.native
    var Underline: TextEditorCursorStyle = js.native
    var LineThin: TextEditorCursorStyle = js.native
    var BlockOutline: TextEditorCursorStyle = js.native
    var UnderlineThin: TextEditorCursorStyle = js.native
    @JSBracketAccess
    def apply(value: TextEditorCursorStyle): String = js.native
  }

  @js.native
  trait InternalEditorScrollbarOptions extends js.Object {
    def arrowSize: Double = js.native
    def vertical: ScrollbarVisibility = js.native
    def horizontal: ScrollbarVisibility = js.native
    def useShadows: Boolean = js.native
    def verticalHasArrows: Boolean = js.native
    def horizontalHasArrows: Boolean = js.native
    def handleMouseWheel: Boolean = js.native
    def horizontalScrollbarSize: Double = js.native
    def horizontalSliderSize: Double = js.native
    def verticalScrollbarSize: Double = js.native
    def verticalSliderSize: Double = js.native
    def mouseWheelScrollSensitivity: Double = js.native
  }

  @js.native
  trait InternalEditorMinimapOptions extends js.Object {
    def enabled: Boolean = js.native
    def side: String = js.native
    def showSlider: String = js.native
    def renderCharacters: Boolean = js.native
    def maxColumn: Double = js.native
  }

  @js.native
  trait InternalEditorFindOptions extends js.Object {
    def seedSearchStringFromSelection: Boolean = js.native
    def autoFindInSelection: Boolean = js.native
  }

  @js.native
  trait EditorWrappingInfo extends js.Object {
    def inDiffEditor: Boolean = js.native
    def isDominatedByLongLines: Boolean = js.native
    def isWordWrapMinified: Boolean = js.native
    def isViewportWrapping: Boolean = js.native
    def wrappingColumn: Double = js.native
    def wrappingIndent: WrappingIndent = js.native
    def wordWrapBreakBeforeCharacters: String = js.native
    def wordWrapBreakAfterCharacters: String = js.native
    def wordWrapBreakObtrusiveCharacters: String = js.native
  }

  @js.native
  sealed trait RenderLineNumbersType extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.RenderLineNumbersType")
  object RenderLineNumbersType extends js.Object {
    var Off: RenderLineNumbersType = js.native
    var On: RenderLineNumbersType = js.native
    var Relative: RenderLineNumbersType = js.native
    var Interval: RenderLineNumbersType = js.native
    var Custom: RenderLineNumbersType = js.native
    @JSBracketAccess
    def apply(value: RenderLineNumbersType): String = js.native
  }

  @js.native
  trait InternalEditorViewOptions extends js.Object {
    def extraEditorClassName: String = js.native
    def disableMonospaceOptimizations: Boolean = js.native
    def rulers: js.Array[Double] = js.native
    def ariaLabel: String = js.native
    def renderLineNumbers: RenderLineNumbersType = js.native
    def renderCustomLineNumbers: js.Function1[Double, String] = js.native
    def selectOnLineNumbers: Boolean = js.native
    def glyphMargin: Boolean = js.native
    def revealHorizontalRightPadding: Double = js.native
    def roundedSelection: Boolean = js.native
    def overviewRulerLanes: Double = js.native
    def overviewRulerBorder: Boolean = js.native
    def cursorBlinking: TextEditorCursorBlinkingStyle = js.native
    def mouseWheelZoom: Boolean = js.native
    def cursorStyle: TextEditorCursorStyle = js.native
    def cursorWidth: Double = js.native
    def hideCursorInOverviewRuler: Boolean = js.native
    def scrollBeyondLastLine: Boolean = js.native
    def smoothScrolling: Boolean = js.native
    def stopRenderingLineAfter: Double = js.native
    def renderWhitespace: String = js.native
    def renderControlCharacters: Boolean = js.native
    def fontLigatures: Boolean = js.native
    def renderIndentGuides: Boolean = js.native
    def renderLineHighlight: String = js.native
    def scrollbar: InternalEditorScrollbarOptions = js.native
    def minimap: InternalEditorMinimapOptions = js.native
    def fixedOverflowWidgets: Boolean = js.native
  }

  @js.native
  trait EditorContribOptions extends js.Object {
    def selectionClipboard: Boolean = js.native
    def hover: Boolean = js.native
    def links: Boolean = js.native
    def contextmenu: Boolean = js.native
    def quickSuggestions: Boolean | js.Any = js.native
    def quickSuggestionsDelay: Double = js.native
    def parameterHints: Boolean = js.native
    def iconsInSuggestions: Boolean = js.native
    def formatOnType: Boolean = js.native
    def formatOnPaste: Boolean = js.native
    def suggestOnTriggerCharacters: Boolean = js.native
    def acceptSuggestionOnEnter: String = js.native
    def acceptSuggestionOnCommitCharacter: Boolean = js.native
    def snippetSuggestions: String = js.native
    def wordBasedSuggestions: Boolean = js.native
    def suggestSelection: String = js.native
    def suggestFontSize: Double = js.native
    def suggestLineHeight: Double = js.native
    def selectionHighlight: Boolean = js.native
    def occurrencesHighlight: Boolean = js.native
    def codeLens: Boolean = js.native
    def folding: Boolean = js.native
    def foldingStrategy: String = js.native
    def showFoldingControls: String = js.native
    def matchBrackets: Boolean = js.native
    def find: InternalEditorFindOptions = js.native
    def colorDecorators: Boolean = js.native
    def lightbulbEnabled: Boolean = js.native
    def codeActionsOnSave: ICodeActionsOnSaveOptions = js.native
    def codeActionsOnSaveTimeout: Double = js.native
  }

  @js.native
  @JSGlobal("monaco.editor.InternalEditorOptions")
  class InternalEditorOptions extends js.Object {
    def _internalEditorOptionsBrand: Unit = js.native
    def canUseLayerHinting: Boolean = js.native
    def pixelRatio: Double = js.native
    def editorClassName: String = js.native
    def lineHeight: Double = js.native
    def readOnly: Boolean = js.native
    def multiCursorModifier: String = js.native
    def multiCursorMergeOverlapping: Boolean = js.native
    def wordSeparators: String = js.native
    def autoClosingBrackets: Boolean = js.native
    def autoIndent: Boolean = js.native
    def useTabStops: Boolean = js.native
    def tabFocusMode: Boolean = js.native
    def dragAndDrop: Boolean = js.native
    def emptySelectionClipboard: Boolean = js.native
    def layoutInfo: EditorLayoutInfo = js.native
    def fontInfo: FontInfo = js.native
    def viewInfo: InternalEditorViewOptions = js.native
    def wrappingInfo: EditorWrappingInfo = js.native
    def contribInfo: EditorContribOptions = js.native
  }

  @js.native
  trait OverviewRulerPosition extends js.Object {
    def width: Double = js.native
    def height: Double = js.native
    def top: Double = js.native
    def right: Double = js.native
  }

  @js.native
  trait EditorLayoutInfo extends js.Object {
    def width: Double = js.native
    def height: Double = js.native
    def glyphMarginLeft: Double = js.native
    def glyphMarginWidth: Double = js.native
    def glyphMarginHeight: Double = js.native
    def lineNumbersLeft: Double = js.native
    def lineNumbersWidth: Double = js.native
    def lineNumbersHeight: Double = js.native
    def decorationsLeft: Double = js.native
    def decorationsWidth: Double = js.native
    def decorationsHeight: Double = js.native
    def contentLeft: Double = js.native
    def contentWidth: Double = js.native
    def contentHeight: Double = js.native
    def minimapLeft: Double = js.native
    def minimapWidth: Double = js.native
    def renderMinimap: RenderMinimap = js.native
    def viewportColumn: Double = js.native
    def verticalScrollbarWidth: Double = js.native
    def horizontalScrollbarHeight: Double = js.native
    def overviewRuler: OverviewRulerPosition = js.native
  }

  @js.native
  trait IConfigurationChangedEvent extends js.Object {
    def canUseLayerHinting: Boolean = js.native
    def pixelRatio: Boolean = js.native
    def editorClassName: Boolean = js.native
    def lineHeight: Boolean = js.native
    def readOnly: Boolean = js.native
    def accessibilitySupport: Boolean = js.native
    def multiCursorModifier: Boolean = js.native
    def multiCursorMergeOverlapping: Boolean = js.native
    def wordSeparators: Boolean = js.native
    def autoClosingBrackets: Boolean = js.native
    def autoIndent: Boolean = js.native
    def useTabStops: Boolean = js.native
    def tabFocusMode: Boolean = js.native
    def dragAndDrop: Boolean = js.native
    def emptySelectionClipboard: Boolean = js.native
    def layoutInfo: Boolean = js.native
    def fontInfo: Boolean = js.native
    def viewInfo: Boolean = js.native
    def wrappingInfo: Boolean = js.native
    def contribInfo: Boolean = js.native
  }

  @js.native
  trait IViewZone extends js.Object {
    var afterLineNumber: Double = js.native
    var afterColumn: Double = js.native
    var suppressMouseDown: Boolean = js.native
    var heightInLines: Double = js.native
    var heightInPx: Double = js.native
    var domNode: HTMLElement = js.native
    var marginDomNode: HTMLElement = js.native
    var onDomNodeTop: js.Function1[Double, Unit] = js.native
    var onComputedHeight: js.Function1[Double, Unit] = js.native
  }

  @js.native
  trait IViewZoneChangeAccessor extends js.Object {
    def addZone(zone: IViewZone): Double = js.native
    def removeZone(id: Double): Unit = js.native
    def layoutZone(id: Double): Unit = js.native
  }

  @js.native
  sealed trait ContentWidgetPositionPreference extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.ContentWidgetPositionPreference")
  object ContentWidgetPositionPreference extends js.Object {
    var EXACT: ContentWidgetPositionPreference = js.native
    var ABOVE: ContentWidgetPositionPreference = js.native
    var BELOW: ContentWidgetPositionPreference = js.native
    @JSBracketAccess
    def apply(value: ContentWidgetPositionPreference): String = js.native
  }

  @js.native
  trait IContentWidgetPosition extends js.Object {
    var position: IPosition = js.native
    var preference: js.Array[ContentWidgetPositionPreference] = js.native
  }

  @js.native
  trait IContentWidget extends js.Object {
    var allowEditorOverflow: Boolean = js.native
    var suppressMouseDown: Boolean = js.native
    def getId(): String = js.native
    def getDomNode(): HTMLElement = js.native
    def getPosition(): IContentWidgetPosition = js.native
  }

  @js.native
  sealed trait OverlayWidgetPositionPreference extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.OverlayWidgetPositionPreference")
  object OverlayWidgetPositionPreference extends js.Object {
    var TOP_RIGHT_CORNER: OverlayWidgetPositionPreference = js.native
    var BOTTOM_RIGHT_CORNER: OverlayWidgetPositionPreference = js.native
    var TOP_CENTER: OverlayWidgetPositionPreference = js.native
    @JSBracketAccess
    def apply(value: OverlayWidgetPositionPreference): String = js.native
  }

  @js.native
  trait IOverlayWidgetPosition extends js.Object {
    var preference: OverlayWidgetPositionPreference = js.native
  }

  @js.native
  trait IOverlayWidget extends js.Object {
    def getId(): String = js.native
    def getDomNode(): HTMLElement = js.native
    def getPosition(): IOverlayWidgetPosition = js.native
  }

  @js.native
  sealed trait MouseTargetType extends js.Object {}

  @js.native
  @JSGlobal("monaco.editor.MouseTargetType")
  object MouseTargetType extends js.Object {
    var UNKNOWN: MouseTargetType = js.native
    var TEXTAREA: MouseTargetType = js.native
    var GUTTER_GLYPH_MARGIN: MouseTargetType = js.native
    var GUTTER_LINE_NUMBERS: MouseTargetType = js.native
    var GUTTER_LINE_DECORATIONS: MouseTargetType = js.native
    var GUTTER_VIEW_ZONE: MouseTargetType = js.native
    var CONTENT_TEXT: MouseTargetType = js.native
    var CONTENT_EMPTY: MouseTargetType = js.native
    var CONTENT_VIEW_ZONE: MouseTargetType = js.native
    var CONTENT_WIDGET: MouseTargetType = js.native
    var OVERVIEW_RULER: MouseTargetType = js.native
    var SCROLLBAR: MouseTargetType = js.native
    var OVERLAY_WIDGET: MouseTargetType = js.native
    var OUTSIDE_EDITOR: MouseTargetType = js.native
    @JSBracketAccess
    def apply(value: MouseTargetType): String = js.native
  }

  @js.native
  trait IMouseTarget extends js.Object {
    def element: HTMLElement = js.native
    def `type`: MouseTargetType = js.native
    def position: Position = js.native
    def mouseColumn: Double = js.native
    def range: Range = js.native
    def detail: js.Any = js.native
  }

  @js.native
  trait IEditorMouseEvent extends js.Object {
    def event: IMouseEvent = js.native
    def target: IMouseTarget = js.native
  }

  @js.native
  trait ICodeEditor extends IEditor {
    def onDidChangeModelContent(
        listener: js.Function1[IModelContentChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeModelLanguage(
        listener: js.Function1[IModelLanguageChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeModelLanguageConfiguration(
        listener: js.Function1[IModelLanguageConfigurationChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeModelOptions(
        listener: js.Function1[IModelOptionsChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeConfiguration(
        listener: js.Function1[IConfigurationChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeCursorPosition(
        listener: js.Function1[ICursorPositionChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeCursorSelection(
        listener: js.Function1[ICursorSelectionChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeModel(
        listener: js.Function1[IModelChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidChangeModelDecorations(
        listener: js.Function1[IModelDecorationsChangedEvent, Unit]
    ): IDisposable = js.native
    def onDidFocusEditorText(
        listener: js.Function0[Unit]
    ): IDisposable = js.native
    def onDidBlurEditorText(
        listener: js.Function0[Unit]
    ): IDisposable = js.native
    def onDidFocusEditor(listener: js.Function0[Unit]): IDisposable = js.native
    def onDidBlurEditor(listener: js.Function0[Unit]): IDisposable = js.native
    def onMouseUp(
        listener: js.Function1[IEditorMouseEvent, Unit]
    ): IDisposable = js.native
    def onMouseDown(
        listener: js.Function1[IEditorMouseEvent, Unit]
    ): IDisposable = js.native
    def onContextMenu(
        listener: js.Function1[IEditorMouseEvent, Unit]
    ): IDisposable = js.native
    def onMouseMove(
        listener: js.Function1[IEditorMouseEvent, Unit]
    ): IDisposable = js.native
    def onMouseLeave(
        listener: js.Function1[IEditorMouseEvent, Unit]
    ): IDisposable = js.native
    def onKeyUp(
        listener: js.Function1[IKeyboardEvent, Unit]
    ): IDisposable = js.native
    def onKeyDown(
        listener: js.Function1[IKeyboardEvent, Unit]
    ): IDisposable = js.native
    def onDidLayoutChange(
        listener: js.Function1[EditorLayoutInfo, Unit]
    ): IDisposable = js.native
    def onDidScrollChange(
        listener: js.Function1[IScrollEvent, Unit]
    ): IDisposable = js.native
    override def saveViewState(): ICodeEditorViewState = js.native
    override def restoreViewState(state: ICodeEditorViewState): Unit = js.native
    def hasWidgetFocus(): Boolean = js.native
    def getContribution[T <: IEditorContribution](id: String): T = js.native
    override def getModel(): ITextModel = js.native
    def getConfiguration(): InternalEditorOptions = js.native
    def getValue(options: js.Any = ???): String = js.native
    def setValue(newValue: String): Unit = js.native
    def getScrollWidth(): Double = js.native
    def getScrollLeft(): Double = js.native
    def getScrollHeight(): Double = js.native
    def getScrollTop(): Double = js.native
    def setScrollLeft(newScrollLeft: Double): Unit = js.native
    def setScrollTop(newScrollTop: Double): Unit = js.native
    def setScrollPosition(position: INewScrollPosition): Unit = js.native
    def getAction(id: String): IEditorAction = js.native
    def executeCommand(source: String, command: ICommand): Unit = js.native
    def pushUndoStop(): Boolean = js.native
    def executeEdits(
        source: String,
        edits: js.Array[IIdentifiedSingleEditOperation],
        endCursoState: js.Array[Selection] = ???
    ): Boolean = js.native
    def executeCommands(
        source: String,
        commands: js.Array[ICommand]
    ): Unit = js.native
    def getLineDecorations(
        lineNumber: Double
    ): js.Array[IModelDecoration] = js.native
    def deltaDecorations(
        oldDecorations: js.Array[String],
        newDecorations: js.Array[IModelDeltaDecoration]
    ): js.Array[String] = js.native
    def getLayoutInfo(): EditorLayoutInfo = js.native
    def getVisibleRanges(): js.Array[Range] = js.native
    def getTopForLineNumber(lineNumber: Double): Double = js.native
    def getTopForPosition(
        lineNumber: Double,
        column: Double
    ): Double = js.native
    def getDomNode(): HTMLElement = js.native
    def addContentWidget(widget: IContentWidget): Unit = js.native
    def layoutContentWidget(widget: IContentWidget): Unit = js.native
    def removeContentWidget(widget: IContentWidget): Unit = js.native
    def addOverlayWidget(widget: IOverlayWidget): Unit = js.native
    def layoutOverlayWidget(widget: IOverlayWidget): Unit = js.native
    def removeOverlayWidget(widget: IOverlayWidget): Unit = js.native
    def changeViewZones(
        callback: js.Function1[IViewZoneChangeAccessor, Unit]
    ): Unit = js.native
    def getOffsetForColumn(
        lineNumber: Double,
        column: Double
    ): Double = js.native
    def render(): Unit = js.native
    def getTargetAtClientPoint(
        clientX: Double,
        clientY: Double
    ): IMouseTarget = js.native
    def getScrolledVisiblePosition(position: IPosition): js.Any = js.native
    def applyFontInfo(target: HTMLElement): Unit = js.native
  }

  @js.native
  trait IDiffLineInformation extends js.Object {
    def equivalentLineNumber: Double = js.native
  }

  @js.native
  trait IDiffEditor extends IEditor {
    def getDomNode(): HTMLElement = js.native
    def onDidUpdateDiff(listener: js.Function0[Unit]): IDisposable = js.native
    /*
    def saveViewState(): IDiffEditorViewState = js.native
    def restoreViewState(state: IDiffEditorViewState): Unit = js.native
    def getModel(): IDiffEditorModel = js.native
     */
    def getOriginalEditor(): ICodeEditor = js.native
    def getModifiedEditor(): ICodeEditor = js.native
    def getLineChanges(): js.Array[ILineChange] = js.native
    def getDiffLineInformationForOriginal(
        lineNumber: Double
    ): IDiffLineInformation = js.native
    def getDiffLineInformationForModified(
        lineNumber: Double
    ): IDiffLineInformation = js.native
  }

  @js.native
  @JSGlobal("monaco.editor.FontInfo")
  class FontInfo extends BareFontInfo {
    def _editorStylingBrand: Unit = js.native
    def isTrusted: Boolean = js.native
    def isMonospace: Boolean = js.native
    def typicalHalfwidthCharacterWidth: Double = js.native
    def typicalFullwidthCharacterWidth: Double = js.native
    def spaceWidth: Double = js.native
    def maxDigitWidth: Double = js.native
  }

  @js.native
  @JSGlobal("monaco.editor.BareFontInfo")
  class BareFontInfo extends js.Object {
    def _bareFontInfoBrand: Unit = js.native
    def zoomLevel: Double = js.native
    def fontFamily: String = js.native
    def fontWeight: String = js.native
    def fontSize: Double = js.native
    def lineHeight: Double = js.native
    def letterSpacing: Double = js.native
  }

  @js.native
  @JSGlobal("monaco.editor")
  object Editor extends js.Object {
    def create(
        domElement: HTMLElement,
        options: IEditorConstructionOptions = ???,
        `override`: IEditorOverrideServices = ???
    ): IStandaloneCodeEditor = js.native
    def onDidCreateEditor(
        listener: js.Function1[ICodeEditor, Unit]
    ): IDisposable = js.native
    def createDiffEditor(
        domElement: HTMLElement,
        options: IDiffEditorConstructionOptions = ???,
        `override`: IEditorOverrideServices = ???
    ): IStandaloneDiffEditor = js.native
    def createDiffNavigator(
        diffEditor: IStandaloneDiffEditor,
        opts: IDiffNavigatorOptions = ???
    ): IDiffNavigator = js.native
    // NOTE: Do not call this method, use MetabrowseTextModelService.createModel instead.
    def createModel(
        value: String,
        language: String = ???,
        uri: Uri = ???
    ): ITextModel = js.native
    def setModelLanguage(model: ITextModel, language: String): Unit = js.native
    def setModelMarkers(
        model: ITextModel,
        owner: String,
        markers: js.Array[IMarkerData]
    ): Unit = js.native
    def getModelMarkers(filter: js.Any): js.Array[IMarker] = js.native
    def getModel(uri: Uri): ITextModel = js.native
    def getModels(): js.Array[ITextModel] = js.native
    def onDidCreateModel(
        listener: js.Function1[ITextModel, Unit]
    ): IDisposable = js.native
    def onWillDisposeModel(
        listener: js.Function1[ITextModel, Unit]
    ): IDisposable = js.native
    def onDidChangeModelLanguage(
        listener: js.Function1[js.Any, Unit]
    ): IDisposable = js.native
    def createWebWorker[T](
        opts: IWebWorkerOptions
    ): MonacoWebWorker[T] = js.native
    def colorizeElement(
        domNode: HTMLElement,
        options: IColorizerElementOptions
    ): Promise[Unit] = js.native
    def colorize(
        text: String,
        languageId: String,
        options: IColorizerOptions
    ): Promise[String] = js.native
    def colorizeModelLine(
        model: ITextModel,
        lineNumber: Double,
        tabSize: Double = ???
    ): String = js.native
    def tokenize(
        text: String,
        languageId: String
    ): js.Array[js.Array[Token]] = js.native
    def defineTheme(
        themeName: String,
        themeData: IStandaloneThemeData
    ): Unit = js.native
    def setTheme(themeName: String): Unit = js.native
    type BuiltinTheme = String
    type IColors = js.Dictionary[String]
    // NOTE: Diff model is not used so disable the alias to keep types simpler
    type IEditorModel = ITextModel // | IDiffEditorModel
    type IEditorViewState = ICodeEditorViewState // | IDiffEditorViewState
    type IReadOnlyModel = ITextModel
    type IModel = ITextModel
  }

}

package languages {

  @js.native
  trait IToken extends js.Object {
    var startIndex: Double = js.native
    var scopes: String = js.native
  }

  @js.native
  trait ILineTokens extends js.Object {
    var tokens: js.Array[IToken] = js.native
    var endState: IState = js.native
  }

  @js.native
  trait TokensProvider extends js.Object {
    def getInitialState(): IState = js.native
    def tokenize(line: String, state: IState): ILineTokens = js.native
  }

  @js.native
  trait CodeActionContext extends js.Object {
    def markers: js.Array[editor.IMarkerData] = js.native
    def only: String = js.native
  }

  @js.native
  trait CodeActionProvider extends js.Object {
    def provideCodeActions(
        model: editor.ITextModel,
        range: Range,
        context: CodeActionContext,
        token: CancellationToken
    ): js.Array[Command | CodeAction] | Thenable[
      js.Array[Command | CodeAction]
    ] = js.native
  }

  @js.native
  sealed trait CompletionItemKind extends js.Object {}

  @js.native
  @JSGlobal("monaco.languages.CompletionItemKind")
  object CompletionItemKind extends js.Object {
    var Text: CompletionItemKind = js.native
    var Method: CompletionItemKind = js.native
    var Function: CompletionItemKind = js.native
    var Constructor: CompletionItemKind = js.native
    var Field: CompletionItemKind = js.native
    var Variable: CompletionItemKind = js.native
    var Class: CompletionItemKind = js.native
    var Interface: CompletionItemKind = js.native
    var Module: CompletionItemKind = js.native
    var Property: CompletionItemKind = js.native
    var Unit: CompletionItemKind = js.native
    var Value: CompletionItemKind = js.native
    var Enum: CompletionItemKind = js.native
    var Keyword: CompletionItemKind = js.native
    var Snippet: CompletionItemKind = js.native
    var Color: CompletionItemKind = js.native
    var File: CompletionItemKind = js.native
    var Reference: CompletionItemKind = js.native
    var Folder: CompletionItemKind = js.native
    @JSBracketAccess
    def apply(value: CompletionItemKind): String = js.native
  }

  @js.native
  trait SnippetString extends js.Object {
    var value: String = js.native
  }

  @js.native
  trait CompletionItem extends js.Object {
    var label: String = js.native
    var kind: CompletionItemKind = js.native
    var detail: String = js.native
    var documentation: String | IMarkdownString = js.native
    var command: Command = js.native
    var sortText: String = js.native
    var filterText: String = js.native
    var insertText: String | SnippetString = js.native
    var range: Range = js.native
    var commitCharacters: js.Array[String] = js.native
    var textEdit: editor.ISingleEditOperation = js.native
    var additionalTextEdits: js.Array[editor.ISingleEditOperation] = js.native
  }

  @js.native
  trait CompletionList extends js.Object {
    var isIncomplete: Boolean = js.native
    var items: js.Array[CompletionItem] = js.native
  }

  @js.native
  trait CompletionContext extends js.Object {
    var triggerKind: SuggestTriggerKind = js.native
    var triggerCharacter: String = js.native
  }

  @js.native
  trait CompletionItemProvider extends js.Object {
    var triggerCharacters: js.Array[String] = js.native
    def provideCompletionItems(
        document: editor.ITextModel,
        position: Position,
        token: CancellationToken,
        context: CompletionContext
    ): js.Array[CompletionItem] | Thenable[
      js.Array[CompletionItem]
    ] | CompletionList | Thenable[
      CompletionList
    ] = js.native
    def resolveCompletionItem(
        item: CompletionItem,
        token: CancellationToken
    ): CompletionItem | Thenable[CompletionItem] = js.native
  }

  @js.native
  trait CommentRule extends js.Object {
    var lineComment: String = js.native
    var blockComment: CharacterPair = js.native
  }

  @js.native
  trait LanguageConfiguration extends js.Object {
    var comments: CommentRule = js.native
    var brackets: js.Array[CharacterPair] = js.native
    var wordPattern: RegExp = js.native
    var indentationRules: IndentationRule = js.native
    var onEnterRules: js.Array[OnEnterRule] = js.native
    var autoClosingPairs: js.Array[IAutoClosingPairConditional] = js.native
    var surroundingPairs: js.Array[IAutoClosingPair] = js.native
    var folding: FoldingRules = js.native
    var __electricCharacterSupport: IBracketElectricCharacterContribution = js.native
  }

  @js.native
  trait IndentationRule extends js.Object {
    var decreaseIndentPattern: RegExp = js.native
    var increaseIndentPattern: RegExp = js.native
    var indentNextLinePattern: RegExp = js.native
    var unIndentedLinePattern: RegExp = js.native
  }

  @js.native
  trait FoldingMarkers extends js.Object {
    var start: RegExp = js.native
    var end: RegExp = js.native
  }

  @js.native
  trait FoldingRules extends js.Object {
    var offSide: Boolean = js.native
    var markers: FoldingMarkers = js.native
  }

  @js.native
  trait OnEnterRule extends js.Object {
    var beforeText: RegExp = js.native
    var afterText: RegExp = js.native
    var action: EnterAction = js.native
  }

  @js.native
  trait IBracketElectricCharacterContribution extends js.Object {
    var docComment: IDocComment = js.native
  }

  @js.native
  trait IDocComment extends js.Object {
    var open: String = js.native
    var close: String = js.native
  }

  @js.native
  trait IAutoClosingPair extends js.Object {
    var open: String = js.native
    var close: String = js.native
  }

  @js.native
  trait IAutoClosingPairConditional extends IAutoClosingPair {
    var notIn: js.Array[String] = js.native
  }

  @js.native
  sealed trait IndentAction extends js.Object {}

  @js.native
  @JSGlobal("monaco.languages.IndentAction")
  object IndentAction extends js.Object {
    var None: IndentAction = js.native
    var Indent: IndentAction = js.native
    var IndentOutdent: IndentAction = js.native
    var Outdent: IndentAction = js.native
    @JSBracketAccess
    def apply(value: IndentAction): String = js.native
  }

  @js.native
  trait EnterAction extends js.Object {
    var indentAction: IndentAction = js.native
    var outdentCurrentLine: Boolean = js.native
    var appendText: String = js.native
    var removeText: Double = js.native
  }

  @js.native
  trait IState extends js.Object {
    override def clone(): IState = js.native
    def equals(other: IState): Boolean = js.native
  }

  @js.native
  trait Hover extends js.Object {
    var contents: js.Array[IMarkdownString] = js.native
    var range: IRange = js.native
  }

  @js.native
  trait HoverProvider extends js.Object {
    def provideHover(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): Hover | Thenable[Hover] = js.native
  }

  @js.native
  sealed trait SuggestTriggerKind extends js.Object {}

  @js.native
  @JSGlobal("monaco.languages.SuggestTriggerKind")
  object SuggestTriggerKind extends js.Object {
    var Invoke: SuggestTriggerKind = js.native
    var TriggerCharacter: SuggestTriggerKind = js.native
    var TriggerForIncompleteCompletions: SuggestTriggerKind = js.native
    @JSBracketAccess
    def apply(value: SuggestTriggerKind): String = js.native
  }

  @js.native
  trait CodeAction extends js.Object {
    var title: String = js.native
    var command: Command = js.native
    var edit: WorkspaceEdit = js.native
    var diagnostics: js.Array[editor.IMarkerData] = js.native
    var kind: String = js.native
  }

  @js.native
  trait ParameterInformation extends js.Object {
    var label: String = js.native
    var documentation: String | IMarkdownString = js.native
  }

  @js.native
  trait SignatureInformation extends js.Object {
    var label: String = js.native
    var documentation: String | IMarkdownString = js.native
    var parameters: js.Array[ParameterInformation] = js.native
  }

  @js.native
  trait SignatureHelp extends js.Object {
    var signatures: js.Array[SignatureInformation] = js.native
    var activeSignature: Double = js.native
    var activeParameter: Double = js.native
  }

  @js.native
  trait SignatureHelpProvider extends js.Object {
    var signatureHelpTriggerCharacters: js.Array[String] = js.native
    def provideSignatureHelp(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): SignatureHelp | Thenable[SignatureHelp] = js.native
  }

  @js.native
  sealed trait DocumentHighlightKind extends js.Object {}

  @js.native
  @JSGlobal("monaco.languages.DocumentHighlightKind")
  object DocumentHighlightKind extends js.Object {
    var Text: DocumentHighlightKind = js.native
    var Read: DocumentHighlightKind = js.native
    var Write: DocumentHighlightKind = js.native
    @JSBracketAccess
    def apply(value: DocumentHighlightKind): String = js.native
  }

  @js.native
  trait DocumentHighlight extends js.Object {
    var range: IRange = js.native
    var kind: DocumentHighlightKind = js.native
  }

  @js.native
  trait DocumentHighlightProvider extends js.Object {
    def provideDocumentHighlights(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): js.Array[DocumentHighlight] | Thenable[
      js.Array[DocumentHighlight]
    ] = js.native
  }

  @js.native
  trait ReferenceContext extends js.Object {
    var includeDeclaration: Boolean = js.native
  }

  // @js.native - Non-native to allow extending it in Scala
  trait ReferenceProvider extends js.Object {
    def provideReferences(
        model: editor.ITextModel,
        position: Position,
        context: ReferenceContext,
        token: CancellationToken
    ): js.Array[Location] | Thenable[js.Array[Location]] // = js.native
  }

  @js.native
  trait Location extends js.Object {
    var uri: Uri = js.native
    var range: IRange = js.native
  }

  // @js.native - Non-native to allow extending it in Scala
  trait DefinitionProvider extends js.Object {
    def provideDefinition(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): Definition | Thenable[Definition] // = js.native
  }

  @js.native
  trait ImplementationProvider extends js.Object {
    def provideImplementation(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): Definition | Thenable[Definition] = js.native
  }

  @js.native
  trait TypeDefinitionProvider extends js.Object {
    def provideTypeDefinition(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): Definition | Thenable[Definition] = js.native
  }

  @js.native
  sealed trait SymbolKind extends js.Object {}

  @js.native
  @JSGlobal("monaco.languages.SymbolKind")
  object SymbolKind extends js.Object {
    var File: SymbolKind = js.native
    var Module: SymbolKind = js.native
    var Namespace: SymbolKind = js.native
    var Package: SymbolKind = js.native
    var Class: SymbolKind = js.native
    var Method: SymbolKind = js.native
    var Property: SymbolKind = js.native
    var Field: SymbolKind = js.native
    var Constructor: SymbolKind = js.native
    var Enum: SymbolKind = js.native
    var Interface: SymbolKind = js.native
    var Function: SymbolKind = js.native
    var Variable: SymbolKind = js.native
    var Constant: SymbolKind = js.native
    var String: SymbolKind = js.native
    var Number: SymbolKind = js.native
    var Boolean: SymbolKind = js.native
    var Array: SymbolKind = js.native
    var Object: SymbolKind = js.native
    var Key: SymbolKind = js.native
    var Null: SymbolKind = js.native
    var EnumMember: SymbolKind = js.native
    var Struct: SymbolKind = js.native
    var Event: SymbolKind = js.native
    var Operator: SymbolKind = js.native
    var TypeParameter: SymbolKind = js.native
    @JSBracketAccess
    def apply(value: SymbolKind): String = js.native
  }

  @js.native
  trait SymbolInformation extends js.Object {
    var name: String = js.native
    var containerName: String = js.native
    var kind: SymbolKind = js.native
    var location: Location = js.native
  }

  // @js.native - Non-native to allow extending it in Scala
  trait DocumentSymbolProvider extends js.Object {
    def provideDocumentSymbols(
        model: editor.ITextModel,
        token: CancellationToken
    ): js.Array[SymbolInformation] | Thenable[
      js.Array[SymbolInformation]
    ] // = js.native
  }

  @js.native
  trait TextEdit extends js.Object {
    var range: IRange = js.native
    var text: String = js.native
    var eol: editor.EndOfLineSequence = js.native
  }

  @js.native
  trait FormattingOptions extends js.Object {
    var tabSize: Double = js.native
    var insertSpaces: Boolean = js.native
  }

  @js.native
  trait DocumentFormattingEditProvider extends js.Object {
    def provideDocumentFormattingEdits(
        model: editor.ITextModel,
        options: FormattingOptions,
        token: CancellationToken
    ): js.Array[TextEdit] | Thenable[js.Array[TextEdit]] = js.native
  }

  @js.native
  trait DocumentRangeFormattingEditProvider extends js.Object {
    def provideDocumentRangeFormattingEdits(
        model: editor.ITextModel,
        range: Range,
        options: FormattingOptions,
        token: CancellationToken
    ): js.Array[TextEdit] | Thenable[js.Array[TextEdit]] = js.native
  }

  @js.native
  trait OnTypeFormattingEditProvider extends js.Object {
    var autoFormatTriggerCharacters: js.Array[String] = js.native
    def provideOnTypeFormattingEdits(
        model: editor.ITextModel,
        position: Position,
        ch: String,
        options: FormattingOptions,
        token: CancellationToken
    ): js.Array[TextEdit] | Thenable[js.Array[TextEdit]] = js.native
  }

  @js.native
  trait ILink extends js.Object {
    var range: IRange = js.native
    var url: String = js.native
  }

  @js.native
  trait LinkProvider extends js.Object {
    def provideLinks(
        model: editor.ITextModel,
        token: CancellationToken
    ): js.Array[ILink] | Thenable[js.Array[ILink]] = js.native
    var resolveLink: js.Function2[ILink, CancellationToken, ILink | Thenable[
      ILink
    ]] = js.native
  }

  @js.native
  trait IColor extends js.Object {
    def red: Double = js.native
    def green: Double = js.native
    def blue: Double = js.native
    def alpha: Double = js.native
  }

  @js.native
  trait IColorPresentation extends js.Object {
    var label: String = js.native
    var textEdit: TextEdit = js.native
    var additionalTextEdits: js.Array[TextEdit] = js.native
  }

  @js.native
  trait IColorInformation extends js.Object {
    var range: IRange = js.native
    var color: IColor = js.native
  }

  @js.native
  trait DocumentColorProvider extends js.Object {
    def provideDocumentColors(
        model: editor.ITextModel,
        token: CancellationToken
    ): js.Array[IColorInformation] | Thenable[
      js.Array[IColorInformation]
    ] = js.native
    def provideColorPresentations(
        model: editor.ITextModel,
        colorInfo: IColorInformation,
        token: CancellationToken
    ): js.Array[IColorPresentation] | Thenable[
      js.Array[IColorPresentation]
    ] = js.native
  }

  @js.native
  trait FoldingContext extends js.Object {}

  @js.native
  trait FoldingRangeProvider extends js.Object {
    def provideFoldingRanges(
        model: editor.ITextModel,
        context: FoldingContext,
        token: CancellationToken
    ): js.Array[FoldingRange] | Thenable[js.Array[FoldingRange]] = js.native
  }

  @js.native
  trait FoldingRange extends js.Object {
    var start: Double = js.native
    var end: Double = js.native
    var kind: FoldingRangeKind = js.native
  }

  @js.native
  @JSGlobal("monaco.languages.FoldingRangeKind")
  class FoldingRangeKind protected () extends js.Object {
    def this(value: String) = this()
    var value: String = js.native
  }

  @js.native
  @JSGlobal("monaco.languages.FoldingRangeKind")
  object FoldingRangeKind extends js.Object {
    def Comment: FoldingRangeKind = js.native
    def Imports: FoldingRangeKind = js.native
    def Region: FoldingRangeKind = js.native
  }

  @js.native
  trait ResourceFileEdit extends js.Object {
    var oldUri: Uri = js.native
    var newUri: Uri = js.native
  }

  @js.native
  trait ResourceTextEdit extends js.Object {
    var resource: Uri = js.native
    var modelVersionId: Double = js.native
    var edits: js.Array[TextEdit] = js.native
  }

  @js.native
  trait WorkspaceEdit extends js.Object {
    var edits: js.Array[ResourceTextEdit | ResourceFileEdit] = js.native
    var rejectReason: String = js.native
  }

  @js.native
  trait RenameLocation extends js.Object {
    var range: IRange = js.native
    var text: String = js.native
  }

  @js.native
  trait RenameProvider extends js.Object {
    def provideRenameEdits(
        model: editor.ITextModel,
        position: Position,
        newName: String,
        token: CancellationToken
    ): WorkspaceEdit | Thenable[WorkspaceEdit] = js.native
    def resolveRenameLocation(
        model: editor.ITextModel,
        position: Position,
        token: CancellationToken
    ): RenameLocation | Thenable[RenameLocation] = js.native
  }

  @js.native
  trait Command extends js.Object {
    var id: String = js.native
    var title: String = js.native
    var tooltip: String = js.native
    var arguments: js.Array[js.Any] = js.native
  }

  @js.native
  trait ICodeLensSymbol extends js.Object {
    var range: IRange = js.native
    var id: String = js.native
    var command: Command = js.native
  }

  @js.native
  trait CodeLensProvider extends js.Object {
    var onDidChange: IEvent[this.type] = js.native
    def provideCodeLenses(
        model: editor.ITextModel,
        token: CancellationToken
    ): js.Array[ICodeLensSymbol] | Thenable[
      js.Array[ICodeLensSymbol]
    ] = js.native
    def resolveCodeLens(
        model: editor.ITextModel,
        codeLens: ICodeLensSymbol,
        token: CancellationToken
    ): ICodeLensSymbol | Thenable[ICodeLensSymbol] = js.native
  }

  @js.native
  trait ILanguageExtensionPoint extends js.Object {
    var id: String = js.native
    var extensions: js.Array[String] = js.native
    var filenames: js.Array[String] = js.native
    var filenamePatterns: js.Array[String] = js.native
    var firstLine: String = js.native
    var aliases: js.Array[String] = js.native
    var mimetypes: js.Array[String] = js.native
    var configuration: String = js.native
  }

  @js.native
  trait IMonarchLanguage extends js.Object {
    var tokenizer: IMonarchLanguage.Tokenizer = js.native
    var ignoreCase: Boolean = js.native
    var defaultToken: String = js.native
    var brackets: js.Array[IMonarchLanguageBracket] = js.native
    var start: String = js.native
    var tokenPostfix: String = js.native
  }

  object IMonarchLanguage {

    @js.native
    trait Tokenizer extends js.Object {
      @JSBracketAccess
      def apply(name: String): js.Array[IMonarchLanguageRule] = js.native
      @JSBracketAccess
      def update(
          name: String,
          v: js.Array[IMonarchLanguageRule]
      ): Unit = js.native
    }
  }

  @js.native
  trait IMonarchLanguageRule extends js.Object {
    var regex: String | RegExp = js.native
    var action: IMonarchLanguageAction = js.native
    var include: String = js.native
  }

  @js.native
  trait IMonarchLanguageAction extends js.Object {
    var group: js.Array[IMonarchLanguageAction] = js.native
    var cases: Object = js.native
    var token: String = js.native
    var next: String = js.native
    var switchTo: String = js.native
    var goBack: Double = js.native
    var bracket: String = js.native
    var nextEmbedded: String = js.native
    var log: String = js.native
  }

  @js.native
  trait IMonarchLanguageBracket extends js.Object {
    var open: String = js.native
    var close: String = js.native
    var token: String = js.native
  }

  @js.native
  @JSGlobal("monaco.languages")
  object Languages extends js.Object {
    def register(language: ILanguageExtensionPoint): Unit = js.native
    def getLanguages(): js.Array[ILanguageExtensionPoint] = js.native
    def onLanguage(
        languageId: String,
        callback: js.Function0[Unit]
    ): IDisposable = js.native
    def setLanguageConfiguration(
        languageId: String,
        configuration: LanguageConfiguration
    ): IDisposable = js.native
    def setTokensProvider(
        languageId: String,
        provider: TokensProvider
    ): IDisposable = js.native
    def setMonarchTokensProvider(
        languageId: String,
        languageDef: IMonarchLanguage
    ): IDisposable = js.native
    def registerReferenceProvider(
        languageId: String,
        provider: ReferenceProvider
    ): IDisposable = js.native
    def registerRenameProvider(
        languageId: String,
        provider: RenameProvider
    ): IDisposable = js.native
    def registerSignatureHelpProvider(
        languageId: String,
        provider: SignatureHelpProvider
    ): IDisposable = js.native
    def registerHoverProvider(
        languageId: String,
        provider: HoverProvider
    ): IDisposable = js.native
    def registerDocumentSymbolProvider(
        languageId: String,
        provider: DocumentSymbolProvider
    ): IDisposable = js.native
    def registerDocumentHighlightProvider(
        languageId: String,
        provider: DocumentHighlightProvider
    ): IDisposable = js.native
    def registerDefinitionProvider(
        languageId: String,
        provider: DefinitionProvider
    ): IDisposable = js.native
    def registerImplementationProvider(
        languageId: String,
        provider: ImplementationProvider
    ): IDisposable = js.native
    def registerTypeDefinitionProvider(
        languageId: String,
        provider: TypeDefinitionProvider
    ): IDisposable = js.native
    def registerCodeLensProvider(
        languageId: String,
        provider: CodeLensProvider
    ): IDisposable = js.native
    def registerCodeActionProvider(
        languageId: String,
        provider: CodeActionProvider
    ): IDisposable = js.native
    def registerDocumentFormattingEditProvider(
        languageId: String,
        provider: DocumentFormattingEditProvider
    ): IDisposable = js.native
    def registerDocumentRangeFormattingEditProvider(
        languageId: String,
        provider: DocumentRangeFormattingEditProvider
    ): IDisposable = js.native
    def registerOnTypeFormattingEditProvider(
        languageId: String,
        provider: OnTypeFormattingEditProvider
    ): IDisposable = js.native
    def registerLinkProvider(
        languageId: String,
        provider: LinkProvider
    ): IDisposable = js.native
    def registerCompletionItemProvider(
        languageId: String,
        provider: CompletionItemProvider
    ): IDisposable = js.native
    def registerColorProvider(
        languageId: String,
        provider: DocumentColorProvider
    ): IDisposable = js.native
    def registerFoldingRangeProvider(
        languageId: String,
        provider: FoldingRangeProvider
    ): IDisposable = js.native
    type CharacterPair = js.Tuple2[String, String]
    type Definition = Location | js.Array[Location]
  }

}

package worker {

  @js.native
  trait IMirrorModel extends js.Object {
    def uri: Uri = js.native
    def version: Double = js.native
    def getValue(): String = js.native
  }

  @js.native
  trait IWorkerContext extends js.Object {
    def getMirrorModels(): js.Array[IMirrorModel] = js.native
  }

}

@js.native
@JSGlobal("monaco")
object Monaco extends js.Object {
  type Thenable[T] = PromiseLike[T]
  type TValueCallback[T] = js.Function1[T | PromiseLike[T], Unit]
  type ProgressCallback[TProgress] = js.Function1[TProgress, Unit]
}
