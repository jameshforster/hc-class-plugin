package models

import scala.util.{Success, Try}

/**
  * Created by james-forster on 31/08/16.
  */

case class PlayerJob (job: Job, level: Int, experience: Int) {
  val playerJobToString = s"job=${job.jobName}&level=${level.toString}&experience=${experience.toString}"
}

object PlayerJob {

  val stringToPlayerJob: String => PlayerJob = input => {
    val inputArray = input.split("&")
    val jobName = inputArray.apply(0).stripPrefix("job=")
    val level = Try(inputArray.apply(1).stripPrefix("level=").toInt) match {
      case Success(value) => value
      case _ => 0
    }
    val experience = Try(inputArray.apply(2).stripPrefix("experience=").toInt) match {
      case Success(value) => value
      case _ => 0
    }
    val matchedJob = Jobs.jobsList.filter(_.jobName.equals(jobName))

    if (matchedJob.nonEmpty) PlayerJob(matchedJob.head, level, experience)
    else PlayerJob(Jobs.Jobless, 0, 0)
  }
}

