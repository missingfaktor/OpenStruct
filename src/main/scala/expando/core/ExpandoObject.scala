package expando.core

import language.dynamics
import collection.mutable
import mutable.{Builder, MapBuilder}
import collection.generic.CanBuildFrom
import expando.util.ForwardingMutableMap

class ExpandoObject
    extends ForwardingMutableMap[String, Any, ExpandoObject]
    with Dynamic
    with Proxy {

  protected val delegate = mutable.Map.empty[String, Any]
  override def _empty = ExpandoObject.empty

  def selectDynamic(key: String): Any = this(key)

  def updateDynamic(key: String)(value: Any) = {
    this(key) = value
    this
  }

  override def toString = {
    val repr = this.map({case (k, v) => s"$k: $v"}).mkString("{", ", ", "}")
    s"ExpandoObject($repr)"
  }

  override def self: Any = delegate
}

object ExpandoObject {
  def empty = new ExpandoObject

  def apply(kvs: (String, Any)*): ExpandoObject = {
    kvs.foldLeft(empty)(_ += _)
  }

  def newBuilder: Builder[(String, Any), ExpandoObject] = {
    new MapBuilder[String, Any, ExpandoObject](empty)
  }

  def withUnderlyingObject(underlying: Any) = new ExpandoObjectPlus(underlying)

  implicit def canBuildFrom: CanBuildFrom[ExpandoObject, (String, Any), ExpandoObject] = {
    new CanBuildFrom[ExpandoObject, (String, Any), ExpandoObject] {
      def apply(from: ExpandoObject) = newBuilder
      def apply() = newBuilder
    }
  }
}
