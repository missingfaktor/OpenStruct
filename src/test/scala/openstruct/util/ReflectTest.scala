package openstruct.util

import org.specs2.mutable.Specification

class ReflectTest extends Specification {
  // Wow, the test fails without this flag. Baffling! I hope the Scala reflection library becomes thread safe soon.
  sequential
  fullStackTrace

  class Foo {
    val publicField = 'puf
    private val privateField = 'prf
    def publicNullaryMethod = 'punm
    private def privateNullaryMethod = 'prnm

    def pitz(x: Int, y: Int) = x + y + 2
  }

  "Reflect" should {
    "get a value of field" in {
      val obj = new Foo
      val r = Reflect on obj
      r.getField("publicField") mustEqual 'puf
    }

    "apply a nullary method" in {
      val obj = new Foo
      val r = Reflect on obj
      r.invokeMethod("publicNullaryMethod") mustEqual 'punm
    }

    "apply a method with arity > 0" in {
      val obj = new Foo
      val r = Reflect on obj
      r.invokeMethod("pitz", 3, 4) mustEqual 9
    }

    "not allow accessing a private field" in {
      val obj = new Foo
      val r = Reflect on obj
      locally {
        r.getField("privateField")
      } must throwAn[Exception]
    }

    "not allow accessing a private method" in {
      val obj = new Foo
      val r = Reflect on obj
      locally {
        r.invokeMethod("privateNullaryMethod")
      } must throwAn[Exception]
    }
  }
}
