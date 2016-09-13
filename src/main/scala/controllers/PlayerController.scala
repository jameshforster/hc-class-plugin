package controllers

import connectors.ServerConnector
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import common.MetadataKeys.PlayerKeys
import models.{Jobs, PlayerJob}

/**
  * Created by james-forster on 13/09/16.
  */

object PlayerController extends PlayerController{
  // $COVERAGE-OFF$
  lazy val plugin = PluginController.main.get
  lazy val serverConnector = ServerConnector
  // $COVERAGE-ON$
}

trait PlayerController {

  val plugin: Plugin
  val serverConnector: ServerConnector

  def getActivePlayerJob(player: Player): Option[PlayerJob] = {
      serverConnector.getPlayerMetaData(player, PlayerKeys.activeJob) match {
        case Some("") => Some(PlayerJob(Jobs.Jobless, 0, 0))
        case Some(data) => Some(PlayerJob.stringToPlayerJob(data))
        case _ => None
      }
  }

}
