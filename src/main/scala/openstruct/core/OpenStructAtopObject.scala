package openstruct.core

import openstruct.util.{FallbackChain, Reflect}

trait OpenStructAtopObject extends OpenStruct with Proxy {
  def underlying: Any

  def selectDynamic(key: String): wrapResultMode.Shape = wrapResultMode wrap {
    val fun = FallbackChain.from[String, Any](
      Reflect.on(underlying).getField(_),
      Reflect.on(underlying).invokeMethod(_),
      this.apply(_)
    )
    fun(key)
  }

  def updateDynamic(key: String)(value: Any): this.type = {
    this(key) = value
    this
  }

  def applyDynamic(name: String)(args: Any*): wrapResultMode.Shape = try {
    val returnedValue = Reflect.on(underlying).invokeMethod(name, args: _*)
    wrapResultMode.wrap(returnedValue)
  } catch {
    case _: ScalaReflectionException =>
      throw new NoSuchElementException(s"Method $name not present in object $underlying.")
  }

  override def self: Any = (underlying, delegate)

  override def toString = {
    s"OpenStruct with underlying object $underlying and backing attribute map $delegate"
  }
}

object OpenStructAtopObject {
  private[openstruct]
  def apply(_underlying: Any, _wrapResults: Boolean = false) = new OpenStructAtopObject {
    def underlying: Any = _underlying
    val wrapResultMode = WrapResultMode.fromBoolean(_wrapResults)
  }
}