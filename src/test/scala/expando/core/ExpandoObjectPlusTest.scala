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
    def add(x: Int) = 2 + x
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

    "propagate method calls down to underlying object" in {
      val f = new Foo
      val e = new ExpandoObjectPlus(f)
      e.add(2) mustEqual 4
    }

    "throw an exception when the method being called is absent in the underlying object" in {
      val f = new Foo
      val e = new ExpandoObjectPlus(f)
      locally {
        e.invalidMethod('arg)
      } must throwA[NoSuchElementException]
    }

    "support structural equality" in {
      case class Bar(i: Int)
      val f1, f2 = new ExpandoObjectPlus(Bar(11))
      f1 mustEqual f2

      f1.name = "foo"
      f1 mustNotEqual f2
    }

    "support sensible hashCode implementation" in {
      case class Bar(i: Int)
      val f1, f2 = new ExpandoObjectPlus(Bar(11))
      f1.## mustEqual f2.##

      f1.name = "foo"
      f1.## mustNotEqual f2.##
    }
  }
}
