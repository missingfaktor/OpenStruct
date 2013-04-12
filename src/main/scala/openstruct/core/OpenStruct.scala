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
}

object OpenStruct {

  def create = new IndependentOpenStruct

  def atop(underlying: Any) = new OpenStructAtopObject(underlying)

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
