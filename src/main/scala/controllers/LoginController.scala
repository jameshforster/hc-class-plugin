package controllers

import models.RolePlayer
import scala.util.{Failure, Success}

/**
  * Created by james-forster on 13/10/16.
  */

object LoginController extends LoginController {
  // $COVERAGE-OFF$
  lazy val playerController = PlayerController
  // $COVERAGE-ON$
}

trait LoginController {

  val playerController: PlayerController

  def login(rolePlayer: RolePlayer): String = {

    if(rolePlayer.newPlayer) {
      (playerController.setActivePlayerJob(rolePlayer), playerController.setAllPlayerJobs(rolePlayer)) match {
        case (Success(_), Success(_)) => s"Welcome to the Alpha ${rolePlayer.player.getName}!"
        case (Failure(error), _) => error.getMessage
        case (_, Failure(error)) => error.getMessage
      }
    }
    else s"Welcome back ${rolePlayer.player.getName}!"
  }
}
