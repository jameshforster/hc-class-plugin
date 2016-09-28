package controllers

import common.MetadataKeys.PlayerKeys
import connectors.ServerConnector
import models.{PlayerJob, RolePlayer}
import org.bukkit.entity.Player
import models.RolePlayer._
import models.PlayerJob._

/**
  * Created by james-forster on 13/09/16.
  */

object PlayerController extends PlayerController {
  // $COVERAGE-OFF$
  lazy val serverConnector = ServerConnector
  // $COVERAGE-ON$
}

trait PlayerController {

  val serverConnector: ServerConnector

  def getActivePlayerJob(player: Player): Option[PlayerJob] = {
    serverConnector.getPlayerMetaData[PlayerJob](player, PlayerKeys.activeJob)
  }

  def setActivePlayerJob(rolePlayer: RolePlayer): Boolean = {
    serverConnector.setPlayerMetaData[PlayerJob](rolePlayer.player, PlayerKeys.activeJob, rolePlayer.activeJob)
  }

  def getAllPlayerJobs(player: Player): Option[Seq[PlayerJob]] = {
    serverConnector.getPlayerMetaData[Seq[PlayerJob]](player, PlayerKeys.allJobs)
  }

  def setAllPlayerJobs(rolePlayer: RolePlayer): Boolean = {
    serverConnector.setPlayerMetaData[Seq[PlayerJob]](rolePlayer.player, PlayerKeys.allJobs, rolePlayer.allJobs)
  }

}
