package models

import org.scalatest.{Matchers, OptionValues, WordSpecLike}
import models.PlayerJob._

/**
  * Created by james-forster on 06/09/16.
  */

class PlayerJobSpec extends AnyRef with WordSpecLike with Matchers with OptionValues {

  "Calling playerJobToString" should {

    "return a Job in string format" in {
      val playerJob = PlayerJob(Jobs.Jobless, 1, 500)
      val result = playerJob.playerJobToString
      result.shouldBe("job=Jobless&level=1&experience=500")
    }
  }

  "Calling stringToPlayerJob" when {

    "provided with a valid string" should {
      val string = "job=Warrior&level=5&experience=500"
      val result = stringToPlayerJob(string)

      "have a job of Warrior" in {
        result.job shouldBe Jobs.Warrior
      }

      "have a level of 5" in {
        result.level shouldBe 5
      }

      "have experience of 500" in {
        result.experience shouldBe 500
      }
    }

    "provided with an invalid string with a non-existent class and invalid values" should {
      val string = "job=Bee&level=a&experience=z"
      val result = stringToPlayerJob(string)

      "have a job of Jobless" in {
        result.job shouldBe Jobs.Jobless
      }

      "have a level of 0" in {
        result.level shouldBe 0
      }

      "have experience of 0" in {
        result.experience shouldBe 0
      }
    }

    "provided with an empty string" should {
      val string = ""
      val result = stringToPlayerJob(string)

      "have a job of Jobless" in {
        result.job shouldBe Jobs.Jobless
      }

      "have a level of 0" in {
        result.level shouldBe 0
      }

      "have experience of 0" in {
        result.experience shouldBe 0
      }
    }

    "provided with a random string" should {
      val string = "fefhuw£$^$$&$%NJNENJ"
      val result = stringToPlayerJob(string)

      "have a job of Jobless" in {
        result.job shouldBe Jobs.Jobless
      }

      "have a level of 0" in {
        result.level shouldBe 0
      }

      "have experience of 0" in {
        result.experience shouldBe 0
      }
    }
  }
}
