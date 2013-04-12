package expando.util

import org.specs2.mutable.Specification

class FallbackChainTest extends Specification {
  def doublesEven(i: Int) = if (i % 2 == 0) 2 * i else sys error "Not even!"
  def tripplesEven(i: Int) = if (i % 2 == 0) 3 * i else sys error "Not even!"
  def tripplesOdd(i: Int) = if (i % 2 != 0) 3 * i else sys error "Not odd!"
  def alwaysPasses(i: Int) = 0
  def alwaysFails(i: Int) = sys error "Bang!"

  "FallbackChain" should {
    "apply first successful function" in {
      val fun = FallbackChain.from[Int, Int](doublesEven, tripplesEven, tripplesOdd)
      fun(2) mustEqual 4
      fun(3) mustEqual 9
    }

    "propagate the last exception when no function succeeds" in {
      val fun = FallbackChain.from[Int, Int](tripplesOdd, alwaysFails)
      fun(2) must throwA[Exception]("Bang!")
    }
  }
}
