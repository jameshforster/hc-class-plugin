package listeners

import controllers.LoginController
import net.md_5.bungee.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.plugin.Plugin

/**
  * Created by james-forster on 12/10/16.
  */
class LoginListener(plugin: Plugin) extends Listener {

  plugin.getServer.getPluginManager.registerEvents(this, plugin)

  @EventHandler
  def onLogin(playerLoginEvent: PlayerLoginEvent): Unit = {
    val player = playerLoginEvent.getPlayer
    val message = LoginController.login(player)
    player.sendMessage(message)
  }
}
