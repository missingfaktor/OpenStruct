package openstruct.core

import openstruct.util.{FallbackChain, Reflect}

trait OpenStructAtopObject extends OpenStruct with Proxy {
  def underlying: Any

  def selectDynamic(key: String): Any = {
    val fun = FallbackChain.from[String, Any](
      Reflect.on(underlying).getField(_),
      Reflect.on(underlying).invokeMethod(_),
      this.apply(_)
    )
    OpenStruct.atop(fun(key))
  }

  def updateDynamic(key: String)(value: Any) = {
    this(key) = value
    this
  }

  def applyDynamic(name: String)(args: Any*) = try {
    val returnedValue = Reflect.on(underlying).invokeMethod(name, args: _*)
    OpenStruct.atop(returnedValue)
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
  def apply(_underlying: Any) = new OpenStructAtopObject {
    def underlying: Any = _underlying
  }
}