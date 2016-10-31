package controllers

import common.AppConfig
import listeners.LoginListener
import net.md_5.bungee.api.CommandSender
import org.bukkit.command.Command
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

  @Override
  def onCommand(sender: CommandSender, command: Command, label: String, args: String*): Boolean = {
    label match {
      case "setClass" => ???
      case "levelUp" => ???
      case "gainExp" => ???
      case _ => false
    }
  }
}
