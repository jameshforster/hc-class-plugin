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

import scala.util.{Failure, Success, Try}

/**
  * Created by james-forster on 13/09/16.
  */
class PlayerControllerSpec extends AnyRef with WordSpecLike with org.scalatest.Matchers with OptionValues with MockitoSugar {

  val illegalArgumentException = new IllegalArgumentException

  def setupTarget(getPlayerJob: Try[PlayerJob], getAllPlayerJobs: Try[Seq[PlayerJob]]) = {

    val mockServerConnector = mock[ServerConnector]
    val mockConfig = mock[AppConfig]

    when(mockServerConnector.getPlayerMetaData[PlayerJob](Matchers.any(), Matchers.eq(PlayerKeys.activeJob))(Matchers.any()))
      .thenReturn(getPlayerJob)

    when(mockServerConnector.getPlayerMetaData[Seq[PlayerJob]](Matchers.any(), Matchers.eq(PlayerKeys.allJobs))(Matchers.any()))
      .thenReturn(getAllPlayerJobs)

    when(mockServerConnector.setPlayerMetaData(Matchers.any(), Matchers.eq(PlayerKeys.activeJob), Matchers.any())(Matchers.any()))
      .thenReturn(if (getPlayerJob.isSuccess) Success() else Failure(illegalArgumentException))

    when(mockServerConnector.setPlayerMetaData(Matchers.any(), Matchers.eq(PlayerKeys.allJobs), Matchers.any())(Matchers.any()))
      .thenReturn(if (getAllPlayerJobs.isSuccess) Success() else Failure(illegalArgumentException))

    new PlayerController {
      override val config: AppConfig = mockConfig
      override val serverConnector: ServerConnector = mockServerConnector
    }
  }

  "Calling getActivePlayerJob" should {

    "return a Failure when an error occurs" in {
      val target = setupTarget(Failure(illegalArgumentException), Failure(illegalArgumentException))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Warrior Job when data is found" in {
      val target = setupTarget(Success("job=Warrior&level=2&experience=5"), Failure(illegalArgumentException))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Success(PlayerJob(Jobs.Warrior, 2, 5))
    }
  }

  "Calling setActivePlayerJob" should {

    "return a Failure if an error occurs" in {
      val target = setupTarget(Failure(illegalArgumentException), Failure(illegalArgumentException))
      val playerJob = PlayerJob(Jobs.Rogue, 1, 0)
      val rolePlayer = RolePlayer(mock[Player], playerJob, Seq())
      val result = target.setActivePlayerJob(rolePlayer)

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Success if data is set correctly" in {
      val target = setupTarget(Success(""), Failure(illegalArgumentException))
      val playerJob = PlayerJob(Jobs.Rogue, 1, 0)
      val rolePlayer = RolePlayer(mock[Player], playerJob, Seq())
      val result = target.setActivePlayerJob(rolePlayer)

      result shouldBe Success()
    }
  }

  "Calling getAllPlayerJobs" should {

    "return a Failure when an error occurs" in {
      val target = setupTarget(Failure(illegalArgumentException), Failure(illegalArgumentException))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Success when data is found" in {
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val target = setupTarget(Failure(illegalArgumentException), Success(allJobs))
      val result = target.getAllPlayerJobs(mock[Player])

      result shouldBe Success(Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42)))
    }
  }

  "Calling setAllPlayerJobs" should {

    "return a Failure if an error occurs" in {
      val target = setupTarget(Failure(illegalArgumentException), Failure(illegalArgumentException))
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val rolePlayer = RolePlayer(mock[Player], mock[PlayerJob], allJobs)
      val result = target.setAllPlayerJobs(rolePlayer)

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Success if data is set correctly" in {
      val target = setupTarget(Failure(illegalArgumentException), Success(Seq()))
      val allJobs = Seq(PlayerJob(Jobs.Warrior,1,10), PlayerJob(Jobs.Rogue,5,42))
      val rolePlayer = RolePlayer(mock[Player], mock[PlayerJob], allJobs)
      val result = target.setAllPlayerJobs(rolePlayer)

      result shouldBe Success()
    }
  }
}
