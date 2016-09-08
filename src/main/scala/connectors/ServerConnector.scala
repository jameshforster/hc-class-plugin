package connectors

import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

import collection.JavaConverters._
import scala.util.Try

/**
  * Created by james-forster on 08/09/16.
  */

object ServerConnector extends ServerConnector {
  val plugin = ???
}

trait ServerConnector {

  val plugin: Plugin

  def updatePlayerMetaData(player: Player, key: String, data: String): Boolean = Try {
    if (player.getMetadata(key).asScala.nonEmpty) player.removeMetadata(key, plugin)
    player.setMetadata(key, new FixedMetadataValue(plugin, data))
  }.isSuccess
}
