package controllers

import common.AppConfig
import listeners.LoginListener
import org.bukkit.plugin.java.JavaPlugin

/**
  * Created by james-forster on 09/09/16.
  */

object PluginController {
  var main: Option[JavaPlugin] = None

  def getConfig: Option[AppConfig] = {
    if (main.isDefined) Some(AppConfig(main.get))
    else None
  }
}

final class Main extends JavaPlugin{

  override def onEnable() = {
    this.saveDefaultConfig()
    this.getDataFolder
    PluginController.main = Some(this)
    new LoginListener(this)
  }

  override def onDisable() = {
    PluginController.main = None
  }
}
