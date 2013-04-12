package expando.core

import org.specs2.mutable.Specification

class ExpandoObjectPlusTest extends Specification {
  class Foo {
    val publicField = 'puf
    private val privateField = 'prf
    def publicNullaryMethod = 'punm
    def privateNullaryMethod = 'prnm
  }

  "ExpandoObjectPlus" should {
    "when looked up for a key which corresponds to a public field, access it" in {
      val f = new Foo
      val e = new ExpandoObjectPlus(f)
      e.publicField mustEqual 'puf
    }

    "when looked up for a key which corresponds to a nullary public method, access it" in {
      val f = new Foo
      val e = new ExpandoObjectPlus(f)
      e.publicNullaryMethod mustEqual 'punm
    }
  }
}
