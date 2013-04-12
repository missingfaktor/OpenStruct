package expando.core

import org.specs2.mutable.Specification

class ExpandoObjectPlusTest extends Specification {
  sequential
  fullStackTrace

  class Foo {
    val publicField = 'puf
    private val privateField = 'prf
    def publicNullaryMethod = 'punm
    private def privateNullaryMethod = 'prnm
  }

  "ExpandoObjectPlus" should {

    "when looked up for a key which corresponds to" in {
      "a public field, access it" in {
        val f = new Foo
        val e = new ExpandoObjectPlus(f)
        e.publicField mustEqual 'puf
      }

      "a nullary public method, access it" in {
        val f = new Foo
        val e = new ExpandoObjectPlus(f)
        e.publicNullaryMethod mustEqual 'punm
      }

      "a private field, ignore it and move ahead" in {
        val f = new Foo
        val e = new ExpandoObjectPlus(f)
        locally {
          e.privateField
        } must throwAn[Exception]

        e.privateField = 'xyz
        e.privateField mustEqual 'xyz
      }

      "a nullary private method, ignore it and move ahead" in {
        val f = new Foo
        val e = new ExpandoObjectPlus(f)
        locally {
          e.privateNullaryMethod
        } must throwAn[Exception]

        e.privateNullaryMethod = 'xyz
        e.privateNullaryMethod mustEqual 'xyz
      }

      "no field or method, but exists in the backing map, access it" in {
        val f = new Foo
        val e = new ExpandoObjectPlus(f)
        e.name = "foo"
        e.name mustEqual "foo"
      }

      "no field or method, nor does it exist in the backing map, throw a NoSuchElementException" in {
        val f = new Foo
        val e = new ExpandoObjectPlus(f)
        locally {
          e.name
        } must throwA[NoSuchElementException]
      }
    }
  }
}
