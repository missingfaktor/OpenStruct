import org.specs2.mutable.Specification

class ExpandoObjectTest extends Specification {
  "ExpandoObject" should {
    "allow inserting of new fields" in {
      val e = new ExpandoObject
      locally {
        e.name = "foo"
      } must not(throwAn[Exception])
    }

    "retrieve inserted fields correctly" in {
      val e = new ExpandoObject
      e.name = "foo"
      e.name mustEqual "foo"
    }

    "throw a NoSuchElementException when the field being access is invalid" in {
      val e = new ExpandoObject
      locally {
        e.bar 
      } must throwA[NoSuchElementException]
    }

    "overwrite older field when a new field with same name is inserted" in {
      val e = new ExpandoObject
      e.name = "foo"
      e.name = 'bar
      e.name mustEqual 'bar
    }

    "retain its type on standard Map operations" in {
      val e = new ExpandoObject
      e.name = "foo"
      e.position = "bar"
      val e2 = e.map({ case (k, v) => k -> v.asInstanceOf[String].toUpperCase})
      e2 must beAnInstanceOf[ExpandoObject]
    }

    "initialize correctly with key-value pairs given upfront" in {
      val e = ExpandoObject("name" -> "foo", "position" -> 10)
      e.name mustEqual "foo"
      e.position mustEqual 10
    }

    "support structural equality" in {
      val e = ExpandoObject("name" -> "foo", "position" -> 10)
      val f = ExpandoObject("name" -> "foo", "position" -> 10)
      e mustEqual f

      f.salary = 'baz
      e mustNotEqual f
    }

    "have a sensible hashCode implementation" in {
      val e = ExpandoObject("name" -> "foo", "position" -> 10)
      val f = ExpandoObject("name" -> "foo", "position" -> 10)
      e.## mustEqual f.##

      f.salary = 'baz
      e.## mustNotEqual f.##
    }
  }
}
