package controllers

import org.scalatest.{Matchers, OptionValues, WordSpecLike}

/**
  * Created by jamez on 13/09/2016.
  */
class ExperienceControllerSpec extends AnyRef with WordSpecLike with Matchers with OptionValues {

  "Calling maxExperience" should {

    "return a 10 with a level of 1" in {
      val result = ExperienceController.maxExperience(1)
      result shouldBe Some(10)
    }

    "return a 51 with a level of 5" in {
      val result = ExperienceController.maxExperience(5)
      result shouldBe Some(51)
    }

    "return a 260 with a level of 9" in {
      val result = ExperienceController.maxExperience(9)
      result shouldBe Some(260)
    }

    "return a 15000 with a level of 19" in {
      val result = ExperienceController.maxExperience(19)
      result shouldBe Some(15000)
    }

    "return a None with a level of 0" in {
      val result = ExperienceController.maxExperience(0)
      result shouldBe None
    }

    "return a None at max level" in {
      val result = ExperienceController.maxExperience(20)
      result shouldBe None
    }
  }
}
