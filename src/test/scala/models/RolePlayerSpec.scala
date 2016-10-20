package models

import common.UnitSpec
import controllers.PlayerController
import org.bukkit.entity.Player
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

import scala.util.{Failure, Success}

/**
  * Created by james-forster on 08/09/16.
  */
class RolePlayerSpec extends UnitSpec with MockitoSugar {

  val exception = new Exception
  val job = PlayerJob(Jobs.Rogue, 1, 1)

  def setupRolePlayer(activeJob: PlayerJob, allJobs: Seq[PlayerJob]): RolePlayer = {

    val mockPlayer = mock[Player]

    new RolePlayer(mockPlayer, activeJob, allJobs)
  }

  def setupRolePlayerMethods(newPlayer: Boolean) : RolePlayerMethods = {

    val mockPlayerController = mock[PlayerController]

    when(mockPlayerController.getActivePlayerJob(Matchers.any()))
      .thenReturn(if (newPlayer) Failure(exception) else Success(job))

    when(mockPlayerController.getAllPlayerJobs(Matchers.any()))
      .thenReturn(if (newPlayer) Failure(exception) else Success(Seq(job)))

    new RolePlayerMethods {
      override val playerController: PlayerController = mockPlayerController
    }
  }


  "A player with only a single warrior job" which {
    val warrior = PlayerJob(Jobs.Warrior, 5, 200)
    val target = setupRolePlayer(warrior, Seq(warrior))

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
    val target = setupRolePlayer(warrior, Seq(warrior, wizard, jobless))

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

  "Creating a RolePlayer from a player" should {

    "create a valid RolePlayer if the player already exists" in {
      val rolePlayer = setupRolePlayerMethods(false)
      val mockPlayer = mock[Player]
      val result = rolePlayer.apply(mockPlayer)

      result shouldBe RolePlayer(mockPlayer, job, Seq(job))
    }

    "create a new RolePlayer if the player is not found" in {
      val rolePlayer = setupRolePlayerMethods(true)
      val mockPlayer = mock[Player]
      val result = rolePlayer.apply(mockPlayer)

      result shouldBe RolePlayer(mockPlayer, PlayerJob(Jobs.Jobless, 0, 0), Seq())
    }
  }
}
