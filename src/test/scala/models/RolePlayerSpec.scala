package models

import org.bukkit.entity.Player
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, OptionValues, WordSpecLike}

/**
  * Created by james-forster on 08/09/16.
  */
class RolePlayerSpec extends AnyRef with WordSpecLike with Matchers with OptionValues with MockitoSugar {

  def setupTarget(activeJob: PlayerJob, allJobs: Seq[PlayerJob]): RolePlayer = {

    val mockPlayer = mock[Player]

    new RolePlayer(mockPlayer, activeJob, allJobs)
  }

  "A player with only a single warrior job" which {
    val warrior = PlayerJob(Jobs.Warrior, 5, 200)
    val target = setupTarget(warrior, Seq(warrior))

    "calls allJobsToString" should {
      val result = RolePlayer.allJobsToString(target.allJobs)

      "return a string containing their only job" in {
        result shouldBe "job=Warrior&level=5&experience=200 "
      }
    }

    "calls stringToAllJobs" should {
      val input = "job=Warrior&level=5&experience=200 "
      val result = RolePlayer.stringToAllJobs(input)

      "should return a Seq with only a single warrior" in {
        result shouldBe Seq(warrior)
      }
    }
  }

  "A player with multiple jobs" which {
    val warrior = PlayerJob(Jobs.Warrior, 5, 200)
    val wizard = PlayerJob(Jobs.Wizard, 1, 0)
    val jobless = PlayerJob(Jobs.Jobless, 0, 0)
    val target = setupTarget(warrior, Seq(warrior, wizard, jobless))

    "calls allJobsToString" should {
      val result = RolePlayer.allJobsToString(target.allJobs)

      "return a string containing all their jobs" in {
        result shouldBe "job=Warrior&level=5&experience=200 job=Wizard&level=1&experience=0 job=Jobless&level=0&experience=0 "
      }
    }

    "calls stringToAllJobs" should {
      val input = "job=Warrior&level=5&experience=200 job=Wizard&level=1&experience=0 job=Jobless&level=0&experience=0 "
      val result = RolePlayer.stringToAllJobs(input)

      "should return a Seq with the warrior and wizard only" in {
        result shouldBe Seq(warrior, wizard)
      }
    }
  }

}
