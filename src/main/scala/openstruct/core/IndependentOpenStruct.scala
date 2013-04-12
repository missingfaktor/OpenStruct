package openstruct.core

trait IndependentOpenStruct extends OpenStruct {
  def selectDynamic(key: String) = OpenStruct.atop(this(key))

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

object IndependentOpenStruct {
  private[openstruct]
  def create = new IndependentOpenStruct {}
}