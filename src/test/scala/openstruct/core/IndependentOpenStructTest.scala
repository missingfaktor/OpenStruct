package openstruct.core

import org.specs2.mutable.Specification

class IndependentOpenStructTest extends Specification {
  sequential
  fullStackTrace

  "Independent OpenStruct" should {
    "allow inserting of new fields" in {
      val e = OpenStruct.create
      locally {
        e.name = "foo"
      } must not(throwAn[Exception])
    }

    "retrieve inserted fields correctly" in {
      val e = OpenStruct.create
      e.name = "foo"
      e.name mustEqual "foo"
    }

    "throw a NoSuchElementException when the field being access is invalid" in {
      val e = OpenStruct.create
      locally {
        e.bar
      } must throwA[NoSuchElementException]
    }

    "overwrite older field when a new field with same name is inserted" in {
      val e = OpenStruct.create
      e.name = "foo"
      e.name = 'bar
      e.name mustEqual 'bar
    }

    "retain its type on standard Map operations" in {
      val e = OpenStruct.create
      e.name = "foo"
      e.position = "bar"
      val e2 = e.map({ case (k, v) => k -> v.asInstanceOf[String].toUpperCase})
      e2 must beAnInstanceOf[OpenStruct]
    }

    "support structural equality" in {
      val e = OpenStruct("name" -> "foo", "position" -> 10)
      val f = OpenStruct("name" -> "foo", "position" -> 10)
      e mustEqual f

      f.salary = 'baz
      e mustNotEqual f
    }

    "have a sensible hashCode implementation" in {
      val e = OpenStruct("name" -> "foo", "position" -> 10)
      val f = OpenStruct("name" -> "foo", "position" -> 10)
      e.## mustEqual f.##

      f.salary = 'baz
      e.## mustNotEqual f.##
    }
  }
}
