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
      val result = target.allJobsToString

      "return a string containing their only job" in {
        result shouldBe "job=Warrior&level=5&experience=200 "
      }
    }
  }

  "A player with multiple jobs" which {
    val warrior = PlayerJob(Jobs.Warrior, 5, 200)
    val jobless = PlayerJob(Jobs.Jobless, 0, 0)
    val target = setupTarget(warrior, Seq(warrior, jobless))

    "calls allJobsToString" should {
      val result = target.allJobsToString

      "return a string containing all their jobs" in {
        result shouldBe "job=Warrior&level=5&experience=200 job=Jobless&level=0&experience=0 "
      }
    }
  }

}
