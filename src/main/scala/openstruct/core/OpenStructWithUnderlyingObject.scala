package openstruct.core

import openstruct.util.{FallbackChain, Reflect}

class OpenStructWithUnderlyingObject private[openstruct](val underlying: Any) extends OpenStruct with Proxy {

  def selectDynamic(key: String): Any = {
    val fun = FallbackChain.from[String, Any](
      Reflect.on(underlying).getField(_),
      Reflect.on(underlying).invokeMethod(_),
      this.apply(_)
    )
    fun(key)
  }

  def updateDynamic(key: String)(value: Any) = {
    this(key) = value
    this
  }

  def applyDynamic(name: String)(args: Any*): Any = try {
    Reflect.on(underlying).invokeMethod(name, args: _*)
  } catch {
    case _: ScalaReflectionException =>
      throw new NoSuchElementException(s"Method $name not present in object $underlying.")
  }

  override def self: Any = (underlying, delegate)

  override def toString = {
    s"${underlying.toString} with a backing attribute map $delegate"
  }
}