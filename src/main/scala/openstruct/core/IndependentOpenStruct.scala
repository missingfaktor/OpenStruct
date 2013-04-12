package openstruct.core

trait IndependentOpenStruct extends OpenStruct {

  def selectDynamic(key: String): wrapResultMode.Shape = wrapResultMode wrap {
    this(key)
  }

  def updateDynamic(key: String)(value: Any): this.type = {
    this(key) = value
    this
  }

  def applyDynamic(name: String)(args: Any*): wrapResultMode.Shape = throw new UnsupportedOperationException

  override def toString = {
    val repr = this.map({case (k, v) => s"$k: $v"}).mkString("{", ", ", "}")
    s"OpenStruct($repr)"
  }

  override def self: Any = delegate
}

object IndependentOpenStruct {
  private[openstruct]
  def create(_wrapResults: Boolean = false) = new IndependentOpenStruct {
    val wrapResultMode = WrapResultMode.fromBoolean(_wrapResults)
  }
}