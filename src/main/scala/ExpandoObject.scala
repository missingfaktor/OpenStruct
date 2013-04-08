import language.dynamics
import collection.mutable
import mutable.{Builder, MapBuilder}
import collection.generic.CanBuildFrom

class ExpandoObject
    extends ForwardingMutableMap[String, Any, ExpandoObject]
    with Dynamic
    with Proxy {

  protected val delegate = mutable.Map.empty[String, Any]
  override def _empty = new ExpandoObject

  def selectDynamic(key: String): Any = this(key)

  def updateDynamic(key: String)(value: Any): Unit = {
    this(key) = value
  }

  override def toString = {
    val repr = this.map({case (k, v) => s"$k -> $v"}).mkString("{", ", ", "}")
    s"ExpandoObject($repr)"
  }

  override def self = delegate
}

object ExpandoObject {
  def empty = new ExpandoObject

  def apply(kvs: (String, Any)*): ExpandoObject = {
    kvs.foldLeft(empty)(_ += _)
  }

  def newBuilder: Builder[(String, Any), ExpandoObject] = {
    new MapBuilder[String, Any, ExpandoObject](empty)
  }

  implicit def canBuildFrom: CanBuildFrom[ExpandoObject, (String, Any), ExpandoObject] = {
    new CanBuildFrom[ExpandoObject, (String, Any), ExpandoObject] {
      def apply(from: ExpandoObject) = newBuilder
      def apply() = newBuilder
    }
  }
}
