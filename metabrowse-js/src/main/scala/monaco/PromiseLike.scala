package monaco

import scala.scalajs.js
import js.|

/*
 * A native TypeScript type included here since it is used by
 * the bindings.
 */
@js.native
trait PromiseLike[T] extends js.Object {
  def `then`[TResult1, TResult2](
      onfulfilled: js.Function1[
        T,
        TResult1 | PromiseLike[TResult1]
      ] | Unit | Null = ???,
      onrejected: js.Function1[
        js.Any,
        TResult2 | PromiseLike[TResult2]
      ] | Unit | Null = ???
  ): PromiseLike[TResult1 | TResult2] = js.native
}
