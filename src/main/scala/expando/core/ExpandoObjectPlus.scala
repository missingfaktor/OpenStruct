package expando.core

import expando.util.{FallbackChain, Reflect}

class ExpandoObjectPlus(val underlying: Any) extends ExpandoObject {
  override def selectDynamic(key: String): Any = {
    FallbackChain.from[String, Any](
      Reflect.on(underlying).getField(_),
      Reflect.on(underlying).invokeMethod(_),
      super.selectDynamic(_)
    ).apply(key)
  }
}