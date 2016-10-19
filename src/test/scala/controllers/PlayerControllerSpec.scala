package controllers

import common.AppConfig
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

  def setupTarget(getPlayerJob: Either[String, PlayerJob], getAllPlayerJobs: Either[String, Seq[PlayerJob]]) = {

    val mockServerConnector = mock[ServerConnector]
    val mockConfig = mock[AppConfig]

    when(mockServerConnector.getPlayerMetaData[PlayerJob](Matchers.any(), Matchers.eq(PlayerKeys.activeJob))(Matchers.any()))
      .thenReturn(getPlayerJob)

    when(mockServerConnector.getPlayerMetaData[Seq[PlayerJob]](Matchers.any(), Matchers.eq(PlayerKeys.allJobs))(Matchers.any()))
      .thenReturn(getAllPlayerJobs)

    when(mockServerConnector.setPlayerMetaData(Matchers.any(), Matchers.eq(PlayerKeys.activeJob), Matchers.any())(Matchers.any()))
      .thenReturn(getPlayerJob.isRight)

    when(mockServerConnector.setPlayerMetaData(Matchers.any(), Matchers.eq(PlayerKeys.allJobs), Matchers.any())(Matchers.any()))
      .thenReturn(getAllPlayerJobs.isRight)

    new PlayerController {
      override val config: AppConfig = mockConfig
      override val serverConnector: ServerConnector = mockServerConnector
    }
  }

  "Calling getActivePlayerJob" should {

    "return a None when an error occurs" in {
      val target = setupTarget(Left(""), Left(""))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Left("")
    }

    "return Jobless when no data is found" in {
      val target = setupTarget(Right(""), Left(""))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Right(PlayerJob(Jobs.Jobless, 0, 0))
    }

    "return a Warrior Job when data is found" in {
      val target = setupTarget(Right("job=Warrior&level=2&experience=5"), Left(""))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Right(PlayerJob(Jobs.Warrior, 2, 5))
    }
  }

  "Calling setActivePlayerJob" should {

    "return a false if an error occurs" in {
      val target = setupTarget(Left(""), Left(""))
      val playerJob = PlayerJob(Jobs.Rogue, 1, 0)
      val rolePlayer = RolePlayer(mock[Player], playerJob, Seq())
      val result = target.setActivePlayerJob(rolePlayer)

      result shouldBe false
    }

    "return a true if data is set correctly" in {
      val target = setupTarget(Right(""), Left(""))
      val playerJob = PlayerJob(Jobs.Rogue, 1, 0)
      val rolePlayer = RolePlayer(mock[Player], playerJob, Seq())
      val result = target.setActivePlayerJob(rolePlayer)

      result shouldBe true
    }
  }

  "Calling getAllPlayerJobs" should {

    "return a Left when an error occurs" in {
      val target = setupTarget(Left(""), Left(""))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Left("")
    }

    "return an empty Seq when no jobs are found" in {
      val target = setupTarget(Left(""), Right(Seq()))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Right(Seq())
    }

    "return a valid sequence when data is found" in {
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val target = setupTarget(Left(""), Right(allJobs))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Right(Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42)))
    }
  }

  "Calling setAllPlayerJobs" should {

    "return a false if an error occurs" in {
      val target = setupTarget(Left(""), Left(""))
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val rolePlayer = RolePlayer(mock[Player], mock[PlayerJob], allJobs)
      val result = target.setAllPlayerJobs(rolePlayer)

      result shouldBe false
    }

    "return a true if data is set correctly" in {
      val target = setupTarget(Left(""), Right(Seq()))
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val rolePlayer = RolePlayer(mock[Player], mock[PlayerJob], allJobs)
      val result = target.setAllPlayerJobs(rolePlayer)

      result shouldBe true
    }
  }
}
