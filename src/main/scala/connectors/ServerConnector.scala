package connectors

import controllers.PluginController
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

import collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
  * Created by james-forster on 08/09/16.
  */

object ServerConnector extends ServerConnector {
  // $COVERAGE-OFF$
  lazy val plugin = PluginController.main.get
  // $COVERAGE-ON$
}

trait ServerConnector {

  val plugin: Plugin

  def setPlayerMetaData(player: Player, key: String, data: String): Boolean = Try {
    player.setMetadata(key, new FixedMetadataValue(plugin, data))
  }.isSuccess

  def removePlayerMetaData(player: Player, key: String): Option[Boolean] = Try {
    if (player.getMetadata(key).asScala.nonEmpty) {
      player.removeMetadata(key, plugin)
      true
    }
    else false
  } match {
    case Success(data) => Some(data)
    case Failure(exception) => None
  }

  def getPlayerMetaData(player: Player, key: String): Option[String] = Try {
    player.getMetadata(key).asScala.filter(result => result.getOwningPlugin.equals(plugin)) match {
      case a if a.nonEmpty => a.head.asString()
      case _ => ""
    }
  } match {
    case Success(data) => Some(data)
    case Failure(exception) => None
  }
}
