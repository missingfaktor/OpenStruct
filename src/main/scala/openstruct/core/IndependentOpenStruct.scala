package openstruct.core

class IndependentOpenStruct private[openstruct]() extends OpenStruct {
  def selectDynamic(key: String): Any = this(key)

  def updateDynamic(key: String)(value: Any) = {
    this(key) = value
    this
  }

  override def toString = {
    val repr = this.map({case (k, v) => s"$k: $v"}).mkString("{", ", ", "}")
    s"OpenStruct($repr)"
  }

  override def self: Any = delegate
}