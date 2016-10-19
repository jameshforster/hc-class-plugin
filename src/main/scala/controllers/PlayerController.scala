package controllers

import java.io.File

import common.AppConfig
import common.MetadataKeys.PlayerKeys
import connectors.ServerConnector
import models.PlayerJob._
import models.RolePlayer._
import models.{PlayerJob, RolePlayer}
import org.bukkit.entity.Player


/**
  * Created by james-forster on 13/09/16.
  */

object PlayerController extends PlayerController {
  // $COVERAGE-OFF$
  lazy val serverConnector = ServerConnector
  lazy val config = PluginController.getConfig.get
  def playerFile(player: Player) = new File(config.pluginLocation + s"/data/players/${player.getName}.json")
  // $COVERAGE-ON$
}

trait PlayerController {

  val serverConnector: ServerConnector
  val config: AppConfig

  def getActivePlayerJob(player: Player): Either[String, PlayerJob] = {
    serverConnector.getPlayerMetaData[PlayerJob](player, PlayerKeys.activeJob)
  }

  def setActivePlayerJob(rolePlayer: RolePlayer): Boolean = {
    serverConnector.setPlayerMetaData[PlayerJob](rolePlayer.player, PlayerKeys.activeJob, rolePlayer.activeJob)
  }

  def getAllPlayerJobs(player: Player): Either[String, Seq[PlayerJob]] = {
    serverConnector.getPlayerMetaData[Seq[PlayerJob]](player, PlayerKeys.allJobs)
  }

  def setAllPlayerJobs(rolePlayer: RolePlayer): Boolean = {
    serverConnector.setPlayerMetaData[Seq[PlayerJob]](rolePlayer.player, PlayerKeys.allJobs, rolePlayer.allJobs)
  }
}
