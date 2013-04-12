package expando.util

import collection.mutable

trait ForwardingMutableMap[K, V, +Self <: mutable.MapLike[K, V, Self] with mutable.Map[K, V]]
    extends mutable.Map[K, V]
    with mutable.MapLike[K, V, Self] { this: Self =>

  override def empty: Self = _empty

  // This is a hack. More here - http://goo.gl/o5ZGs.
  def _empty: Self

  protected val delegate: mutable.Map[K, V]

  def get(key: K): Option[V] = delegate.get(key)

  def iterator: Iterator[(K, V)] = delegate.iterator

  def -=(key: K): this.type = {
    delegate -= key
    this
  }

  def +=(kv: (K, V)): this.type = {
    delegate += kv
    this
  }
}
