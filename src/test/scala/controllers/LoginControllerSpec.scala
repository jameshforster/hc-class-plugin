package controllers

import common.{AppConfig, UnitSpec}
import models.{Jobs, PlayerJob}
import org.bukkit.entity.Player
import org.scalatest.mock.MockitoSugar
import org.mockito.Matchers
import org.mockito.Mockito._

/**
  * Created by james-forster on 13/10/16.
  */
class LoginControllerSpec extends UnitSpec with MockitoSugar {

  def setupPlayer(name: String): Player = {

    val mockPlayer = mock[Player]

    when(mockPlayer.getName)
      .thenReturn(name)

    mockPlayer
  }

  def setupPlayerController(existingPlayer: Boolean): PlayerController = {

    val mockPlayerController = mock[PlayerController]

    when(mockPlayerController.setActivePlayerJob(Matchers.any()))
      .thenReturn(true)

    when(mockPlayerController.setAllPlayerJobs(Matchers.any()))
      .thenReturn(true)

    when(mockPlayerController.getActivePlayerJob(Matchers.any()))
      .thenReturn(if (existingPlayer) Right(PlayerJob(Jobs.Jobless, 0, 0))
      else Left("Not found."))

    when(mockPlayerController.getAllPlayerJobs(Matchers.any()))
      .thenReturn(if (existingPlayer) Right(Seq())
      else Left("Not found"))

    mockPlayerController
  }

  "Calling the login method" when {

    "the player has not logged on before" should {
      val mockPlayerController = setupPlayerController(false)
      val loginController = new LoginController {
        override val playerController: PlayerController = mockPlayerController
        override val config: AppConfig = mock[AppConfig]
      }
      val player = setupPlayer("Overlord59")

      "return a string welcoming the player to the server" in {
        val result = loginController.login(player)

        result shouldBe "Welcome to the Alpha Overlord59!"
      }
    }

    "the player has logged on to the server before" should {
      val mockPlayerController = setupPlayerController(true)
      val loginController = new LoginController {
        override val playerController: PlayerController = mockPlayerController
        override val config: AppConfig = mock[AppConfig]
      }
      val player = setupPlayer("Overlord59")

      "return a string welcoming the player back to the server" in {
        val result = loginController.login(player)

        result shouldBe "Welcome back Overlord59!"
      }
    }
  }
}
