package controllers

import org.bukkit.plugin.java.JavaPlugin

/**
  * Created by james-forster on 09/09/16.
  */

object PluginController {
  var main: Option[JavaPlugin] = None
}

final class Main extends JavaPlugin{

  override def onEnable() = {
    PluginController.main = Some(this)
  }

  override def onDisable() = {
    PluginController.main = None
  }
}
