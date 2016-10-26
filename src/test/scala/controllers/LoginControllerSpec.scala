package controllers

import common.UnitSpec
import models.{Jobs, PlayerJob, RolePlayer}
import org.bukkit.entity.Player
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

import scala.util.{Failure, Success, Try}

/**
  * Created by james-forster on 24/10/16.
  */
class LoginControllerSpec extends UnitSpec with MockitoSugar {

  def setupLoginController(setActiveJob: Try[Unit], setAllJobs: Try[Unit]): LoginController = {

    val mockPlayerController = mock[PlayerController]

    when(mockPlayerController.setActivePlayerJob(Matchers.any()))
      .thenReturn(setActiveJob)

    when(mockPlayerController.setAllPlayerJobs(Matchers.any()))
      .thenReturn(setAllJobs)

    new LoginController {
      override val playerController: PlayerController = mockPlayerController
    }
  }

  def setupRolePlayer(name: String, newPlayer: Boolean): RolePlayer = {

    val mockPlayer = mock[Player]

    when(mockPlayer.getName)
      .thenReturn(name)

    RolePlayer(mockPlayer, PlayerJob(Jobs.Jobless, 0, 0), Seq(), newPlayer)
  }

  val exceptionOne = new Exception("Exception One")
  val exceptionTwo = new Exception("Exception Two")

  "Calling login" should {

    "return a new player message when a new player joins" in {
      val rolePlayer = setupRolePlayer("Overlord59", newPlayer = true)
      val loginController = setupLoginController(Success(()), Success(()))
      val result = loginController.login(rolePlayer)

      result shouldBe  "Welcome to the Alpha Overlord59!"
    }

    "return a returning player message when a previous player joins" in {
      val rolePlayer = setupRolePlayer("Grantharshammer", newPlayer = false)
      val loginController = setupLoginController(Success(()), Success(()))
      val result = loginController.login(rolePlayer)

      result shouldBe "Welcome back Grantharshammer!"
    }

    "return the first error message when it fails to create a new player" in {
      val rolePlayer = setupRolePlayer("Overlord59", newPlayer = true)
      val loginController = setupLoginController(Failure(exceptionOne), Failure(exceptionTwo))
      val result = loginController.login(rolePlayer)

      result shouldBe "Exception One"
    }

    "return the error message when it fails to create a new player with only the first exception" in {
      val rolePlayer = setupRolePlayer("Overlord59", newPlayer = true)
      val loginController = setupLoginController(Failure(exceptionOne), Success(()))
      val result = loginController.login(rolePlayer)

      result shouldBe "Exception One"
    }

    "return the error message when it fails to create a new player with only the second exception" in {
      val rolePlayer = setupRolePlayer("Overlord59", newPlayer = true)
      val loginController = setupLoginController(Success(()), Failure(exceptionTwo))
      val result = loginController.login(rolePlayer)

      result shouldBe "Exception Two"
    }
  }
}
