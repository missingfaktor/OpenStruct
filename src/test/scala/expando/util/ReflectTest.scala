package expando.util

import org.specs2.mutable.Specification

class ReflectTest extends Specification {
  // Wow, the test fails without this flag. Baffling! I hope the Scala reflection library becomes thread safe soon.
  sequential

  class Foo {
    val publicField = 'puf
    private val privateField = 'prf
    def publicNullaryMethod = 'punm
    def privateNullaryMethod = 'prnm
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
