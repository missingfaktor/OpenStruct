package openstruct.util

import util.control.{ControlThrowable, Breaks}

object FallbackChain {
  def from[A, B](funs: (A => B)*): (A => B) = { arg =>
    var value: Option[B] = None
    var lastException: Throwable = new RuntimeException("Empty functions sequence.")
    val outer, inner = new Breaks
    outer.breakable {
      for (f <- funs) {
        inner.breakable {
          try {
            value = Some(f(arg))
            outer.break
          } catch {
            case ex: Throwable if !ex.isInstanceOf[ControlThrowable] => {
              lastException = ex
              inner.break
            }
          }
        }
      }
    }
    value.getOrElse(throw lastException)
  }
}
