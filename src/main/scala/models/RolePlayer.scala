package models

import org.bukkit.entity.Player
import models.PlayerJob._

/**
  * Created by james-forster on 31/08/16.
  */

case class RolePlayer (player: Player, activeJob: PlayerJob, allJobs: Seq[PlayerJob])

object RolePlayer {

  implicit val stringToAllJobs: String => Seq[PlayerJob] = input => {
    val jobsArrayString = input.split(" ")
    jobsArrayString.map(stringToPlayerJob).filterNot(_.job.jobName.equals(Jobs.Jobless.jobName))
  }

  implicit val allJobsToString: Seq[PlayerJob] => String = input => {
    s"${input.foldLeft("")((string, playerJob) => string + PlayerJob.playerJobToString(playerJob) + " ")}"
  }
}

