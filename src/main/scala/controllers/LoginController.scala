package controllers

import org.bukkit.entity.Player

/**
  * Created by james-forster on 13/10/16.
  */

object LoginController extends LoginController {
  // $COVERAGE-OFF$
  lazy val playerController = PlayerController
  // $COVERAGE-ON$
}

trait LoginController {

  val playerController: PlayerController

  def login(player: Player): String = {
    ???
  }
}
