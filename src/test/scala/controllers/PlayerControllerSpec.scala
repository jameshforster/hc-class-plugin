package controllers

import common.MetadataKeys.PlayerKeys
import connectors.ServerConnector
import models.{Jobs, PlayerJob, RolePlayer}
import org.bukkit.entity.Player
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{OptionValues, WordSpecLike}

/**
  * Created by james-forster on 13/09/16.
  */
class PlayerControllerSpec extends AnyRef with WordSpecLike with org.scalatest.Matchers with OptionValues with MockitoSugar {

  def setupTarget(getPlayerJob: Option[String], getAllPlayerJobs: Option[String]) = {

    val mockServerConnector = mock[ServerConnector]

    when(mockServerConnector.getPlayerMetaData(Matchers.any(), Matchers.eq(PlayerKeys.activeJob)))
      .thenReturn(getPlayerJob)

    when(mockServerConnector.getPlayerMetaData(Matchers.any(), Matchers.eq(PlayerKeys.allJobs)))
      .thenReturn(getAllPlayerJobs)

    when(mockServerConnector.setPlayerMetaData(Matchers.any(), Matchers.any(), Matchers.eq(PlayerKeys.activeJob)))
      .thenReturn(getPlayerJob.isDefined)

    when(mockServerConnector.setPlayerMetaData(Matchers.any(), Matchers.any(), Matchers.eq(PlayerKeys.allJobs)))
      .thenReturn(getAllPlayerJobs.isDefined)

    new PlayerController {
      override val serverConnector: ServerConnector = mockServerConnector
    }
  }

  "Calling getActivePlayerJob" should {

    "return a None when an error occurs" in {
      val target = setupTarget(None, None)
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe None
    }

    "return Jobless when no data is found" in {
      val target = setupTarget(Some(""), None)
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Some(PlayerJob(Jobs.Jobless, 0, 0))
    }

    "return a Warrior Job when data is found" in {
      val target = setupTarget(Some("job=Warrior&level=2&experience=5"), None)
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Some(PlayerJob(Jobs.Warrior, 2, 5))
    }
  }

  "Calling setActivePlayerJob" should {

    "return a false if an error occurs" in {
      val target = setupTarget(None, None)
      val playerJob = PlayerJob(Jobs.Rogue, 1, 0)
      val rolePlayer = RolePlayer(mock[Player], playerJob, Seq())
      val result = target.setActivePlayerJob(rolePlayer)

      result shouldBe false
    }

    "return a true if data is set correctly" in {
      val target = setupTarget(Some(""), None)
      val playerJob = PlayerJob(Jobs.Rogue, 1, 0)
      val rolePlayer = RolePlayer(mock[Player], playerJob, Seq())
      val result = target.setActivePlayerJob(rolePlayer)

      result shouldBe true
    }
  }

  "Calling getAllPlayerJobs" should {

    "return a None when an error occurs" in {
      val target = setupTarget(None, None)
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe None
    }

    "return an empty Seq when no jobs are found" in {
      val target = setupTarget(None, Some(""))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Some(Seq())
    }

    "return a valid sequence when data is found" in {
      val allJobs = "job=Warrior&level=1&experience=10 job=Rogue&level=5&experience=42 job=Jobless&level=0&experience=0 invalidString"
      val target = setupTarget(None, Some(allJobs))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Some(Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42)))
    }
  }

  "Calling setAllPlayerJobs" should {

    "return a false if an error occurs" in {
      val target = setupTarget(None, None)
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val rolePlayer = RolePlayer(mock[Player], mock[PlayerJob], allJobs)
      val result = target.setAllPlayerJobs(rolePlayer)

      result shouldBe false
    }

    "return a true if data is set correctly" in {
      val target = setupTarget(None, Some(""))
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val rolePlayer = RolePlayer(mock[Player], mock[PlayerJob], allJobs)
      val result = target.setAllPlayerJobs(rolePlayer)

      result shouldBe true
    }
  }
}
