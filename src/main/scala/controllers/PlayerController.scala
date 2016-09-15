package controllers

import connectors.ServerConnector
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import common.MetadataKeys.PlayerKeys
import models.{Jobs, PlayerJob, RolePlayer}

/**
  * Created by james-forster on 13/09/16.
  */

object PlayerController extends PlayerController{
  // $COVERAGE-OFF$
  lazy val serverConnector = ServerConnector
  // $COVERAGE-ON$
}

trait PlayerController {

  val serverConnector: ServerConnector

  def getActivePlayerJob(player: Player): Option[PlayerJob] = {
      serverConnector.getPlayerMetaData(player, PlayerKeys.activeJob) match {
        case Some("") => Some(PlayerJob(Jobs.Jobless, 0, 0))
        case Some(data) => Some(PlayerJob.stringToPlayerJob(data))
        case _ => None
      }
  }

  def setActivePlayerJob(rolePlayer: RolePlayer): Boolean = {
    serverConnector.setPlayerMetaData(rolePlayer.player, rolePlayer.activeJob.playerJobToString, PlayerKeys.activeJob)
  }

  def getAllPlayerJobs(player: Player): Option[Seq[PlayerJob]] = {
    serverConnector.getPlayerMetaData(player, PlayerKeys.allJobs) match {
      case Some("") => Some(Seq())
      case Some(data) => Some(RolePlayer.stringToAllJobs(data))
      case _ => None
    }
  }

  def setAllPlayerJobs(rolePlayer: RolePlayer): Boolean = {
    serverConnector.setPlayerMetaData(rolePlayer.player, rolePlayer.allJobsToString, PlayerKeys.allJobs)
  }

}
