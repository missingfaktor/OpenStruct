package expando.core

import expando.util.{FallbackChain, Reflect}

class ExpandoObjectPlus(val underlying: Any) extends ExpandoObject with Proxy {
  override def selectDynamic(key: String): Any = {
    FallbackChain.from[String, Any](
      Reflect.on(underlying).getField(_),
      Reflect.on(underlying).invokeMethod(_),
      super.selectDynamic(_)
    ).apply(key)
  }

  def applyDynamic(name: String)(args: Any*): Any = try {
    Reflect.on(underlying).invokeMethod(name, args: _*)
  } catch {
    case _: ScalaReflectionException =>
      throw new NoSuchElementException(s"Method $name not present in object $underlying.")
  }

  override def self: Any = (underlying, delegate)
}