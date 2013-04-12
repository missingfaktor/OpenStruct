package openstruct.core

trait WrapResultMode {
  type Shape
  def wrap(obj: Any): Shape
}

object WrapResultMode {
  def fromBoolean(b: Boolean): WrapResultMode = if (b) True else False

  case object True extends WrapResultMode {
    type Shape = OpenStructAtopObject
    def wrap(obj: Any) = OpenStruct.atop(obj)
  }

  case object False extends WrapResultMode {
    type Shape = Any
    def wrap(obj: Any) = obj
  }
}
