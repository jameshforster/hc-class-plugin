package common

import org.bukkit.plugin.Plugin


/**
  * Created by jamez on 15/09/2016.
  */

case class AppConfig (plugin: Plugin) {
  val storageEnabled: Boolean = plugin.getConfig.getBoolean("storage")
  val databaseHost: String = plugin.getConfig.getString("database.location")
  val databasePort: String = plugin.getConfig.getString("database.port")
}
