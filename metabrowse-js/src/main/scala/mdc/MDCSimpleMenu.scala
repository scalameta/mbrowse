package mdc

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom

/** The MDC Simple Menu component is a spec-aligned drawer component.
  *
  * @see https://material.io/components/web/catalog/menus/
  */
@js.native
@JSImport("@material/menu", "MDCSimpleMenu")
class MDCSimpleMenu extends js.Object {
  def this(element: dom.Element) = this()

  /** Puts the component in the open state.
    */
  var open: Boolean = js.native
}
