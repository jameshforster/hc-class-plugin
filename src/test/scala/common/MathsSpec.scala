package common

import org.scalatest.{Matchers, OptionValues, WordSpecLike}

/**
  * Created by james-forster on 13/09/16.
  */
class MathsSpec extends AnyRef with WordSpecLike with Matchers with OptionValues {

  "Calling negativeToZero" should {

    "return a zero with a negative value" in {
      val result = Maths.negativeToZero(-10)
      result shouldBe 0
    }

    "return a zero with a nil value" in {
      val result = Maths.negativeToZero(0)
      result shouldBe 0
    }

    "return a value with a positive value" in {
      val result = Maths.negativeToZero(8)
      result shouldBe 8
    }
  }

}
