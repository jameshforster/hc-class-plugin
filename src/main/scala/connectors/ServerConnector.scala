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

  def setPlayerMetaData[T](player: Player, key: String, data: T)(implicit writes: T => String): Boolean = Try {
    player.setMetadata(key, new FixedMetadataValue(plugin, data))
  }.isSuccess

  def removePlayerMetaData(player: Player, key: String): Either[String, Boolean] = Try {
    if (player.getMetadata(key).asScala.nonEmpty) {
      player.removeMetadata(key, plugin)
      true
    }
    else false
  } match {
    case Success(data) => Right(data)
    case Failure(exception) => Left("Could not connect to server")
  }

  def getPlayerMetaData[T](player: Player, key: String)(implicit reads: String => T): Either[String, T] = Try {
    player.getMetadata(key).asScala.filter(result => result.getOwningPlugin.equals(plugin)) match {
      case a if a.nonEmpty => a.head.asString()
      case _ => ""
    }
  } match {
    case Success(data) => Right(data)
    case Failure(exception) => Left("Could not connect to server")
  }
}
