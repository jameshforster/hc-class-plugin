package controllers

import common.AppConfig
import models.{Jobs, PlayerJob, RolePlayer}
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerLoginEvent

/**
  * Created by james-forster on 13/10/16.
  */

object LoginController extends LoginController {
  // $COVERAGE-OFF$
  lazy val config = PluginController.getConfig.get
  lazy val playerController = PlayerController
  // $COVERAGE-ON$
}

trait LoginController {

  val config: AppConfig
  val playerController: PlayerController

  def login(player: Player): String = {
    val getActiveJob = playerController.getActivePlayerJob(player)
    val getAllJobs = playerController.getAllPlayerJobs(player)
    (getActiveJob, getAllJobs) match {
      case (Right(_), Right(_)) =>
        s"Welcome back ${player.getName}!"
      case _ =>
        val newPlayer = RolePlayer(player, PlayerJob(Jobs.Jobless, 0, 0), Seq())
        playerController.setActivePlayerJob(newPlayer)
        playerController.setAllPlayerJobs(newPlayer)
        s"Welcome to the Alpha ${newPlayer.player.getName}!"
    }
  }
}
