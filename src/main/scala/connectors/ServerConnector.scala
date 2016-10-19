package connectors

import controllers.PluginController
import models.CustomExceptions._
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

  def setPlayerMetaData[T](player: Player, key: String, data: T)(implicit writes: T => String): Try[Unit] = Try {
    player.setMetadata(key, new FixedMetadataValue(plugin, data))
  }

  def removePlayerMetaData(player: Player, key: String): Try[Unit] = Try {
    if (player.getMetadata(key).asScala.nonEmpty) {
      player.removeMetadata(key, plugin)
    }
    else throw MetaDataNotFoundException(s"Could not find MetaData: $key for player ${player.getName}")
  }

  def getPlayerMetaData[T](player: Player, key: String)(implicit reads: String => T): Try[T] = Try {
    player.getMetadata(key).asScala.filter(result => result.getOwningPlugin.equals(plugin)) match {
      case a if a.nonEmpty => a.head.asString()
      case _ => throw MetaDataNotFoundException(s"Could not find MetaData: $key for player ${player.getName}")
    }
  }
}
