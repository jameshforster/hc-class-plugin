package common

import controllers.PluginController
import models.{Job, Jobs, PlayerJob, RolePlayer}
import spray.json._

import collection.JavaConverters._
import org.bukkit.entity.Player

/**
  * Created by james-forster on 27/09/16.
  */

object JsonConverters extends DefaultJsonProtocol {

  implicit object PlayerFormat extends RootJsonFormat[Player] {
    lazy val plugin = PluginController.main.get

    def write(player: Player) = JsArray(JsString(player.getName))

    def read(json: JsValue): Player = json match {
      case JsArray(Vector(JsString(name))) => plugin.getServer.getOnlinePlayers.asScala.filter(_.getName.equals(name)).head
      case _ => deserializationError("Player not found")
    }
  }

  implicit object JobFormat extends RootJsonFormat[Job] {
    def write(job: Job) = JsArray(JsString(job.jobName))

    def read(json: JsValue): Job = json match {
      case JsArray(Vector(JsString(job))) => Jobs.jobsList.filter(_.jobName.equals(job)).head
      case _ => deserializationError("Job not found")
    }
  }

  implicit val playerJobFormat = jsonFormat3(PlayerJob.apply)

  implicit val rolePlayerFormat = jsonFormat3(RolePlayer.apply)
}
