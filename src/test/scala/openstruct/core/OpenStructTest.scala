package openstruct.core

import org.specs2.mutable.Specification

class OpenStructTest extends Specification {
  sequential
  fullStackTrace

  "OpenStruct" should {

    "initialize correctly with key-value pairs given upfront" in {
      val e = OpenStruct("name" -> "foo", "position" -> 10)
      e.name mustEqual OpenStruct.atop("foo")
      e.position mustEqual OpenStruct.atop(10)
    }
  }
}
