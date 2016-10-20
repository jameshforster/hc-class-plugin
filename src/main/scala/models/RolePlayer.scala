package models

import controllers.PlayerController
import org.bukkit.entity.Player
import models.PlayerJob._

import scala.util.Success

/**
  * Created by james-forster on 31/08/16.
  */

case class RolePlayer (player: Player, activeJob: PlayerJob, allJobs: Seq[PlayerJob])

object RolePlayer extends RolePlayerMethods {

  val playerController = PlayerController

}

trait RolePlayerMethods {

  val playerController: PlayerController

  def apply(player: Player): RolePlayer = {
    val activeJob = playerController.getActivePlayerJob(player)
    val allJobs = playerController.getAllPlayerJobs(player)

    (activeJob, allJobs) match {
      case (Success(job), Success(jobs)) => RolePlayer(player, job, jobs)
      case _ => RolePlayer(player, PlayerJob(Jobs.Jobless, 0, 0), Seq())
    }
  }

  implicit val stringToAllJobs: String => Seq[PlayerJob] = input => {
    val jobsArrayString = input.split(" ")
    jobsArrayString.map(stringToPlayerJob).filterNot(_.job.jobName.equals(Jobs.Jobless.jobName))
  }

  implicit val allJobsToString: Seq[PlayerJob] => String = input => {
    s"${input.foldLeft("")((string, playerJob) => string + PlayerJob.playerJobToString(playerJob) + " ")}"
  }
}

