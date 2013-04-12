package openstruct.core

import language.dynamics
import collection.mutable
import openstruct.util.ForwardingMutableMap
import collection.mutable.{MapBuilder, Builder}
import collection.generic.CanBuildFrom

trait OpenStruct
    extends ForwardingMutableMap[String, Any, OpenStruct]
    with Dynamic
    with Proxy {

  protected val delegate = mutable.Map.empty[String, Any]
  override def _empty = OpenStruct.create

  val wrapResultMode: WrapResultMode
  def selectDynamic(key: String): wrapResultMode.Shape
  def updateDynamic(key: String)(value: Any): this.type
  def applyDynamic(name: String)(args: Any*): wrapResultMode.Shape
}

object OpenStruct {

  def create = IndependentOpenStruct.create()

  def atop(underlying: Any) = OpenStructAtopObject(underlying)

  def apply(kvs: (String, Any)*) = kvs.foldLeft(create)(_ += _)

  // Builder machinery.

  def newBuilder: Builder[(String, Any), OpenStruct] = {
    new MapBuilder[String, Any, OpenStruct](create)
  }

  implicit def canBuildFrom: CanBuildFrom[OpenStruct, (String, Any), OpenStruct] = {
    new CanBuildFrom[OpenStruct, (String, Any), OpenStruct] {
      def apply(from: OpenStruct) = newBuilder
      def apply() = newBuilder
    }
  }
}
