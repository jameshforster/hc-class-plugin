package controllers

import java.math.MathContext

import common.Maths
import models.{PlayerJob, RolePlayer}

/**
  * Created by jamez on 13/09/2016.
  */
object ExperienceController extends ExperienceController{

}

trait ExperienceController {

  def maxExperience(level: Int): Option[Int] = {
    if (level > 0 && level < 20) Some(BigDecimal(10 * math.pow(1.5, level - 1)).round(new MathContext(2)).toInt)
    else None
  }

  def experienceRemaining(level: Int, experience: Int): Option[Int] = {
    maxExperience(level) match {
      case Some(data) => Some(Maths.negativeToZero(data - experience))
      case _ => None
    }
  }

  def gainExperience(rolePlayer: RolePlayer, experience: Int): RolePlayer = {
    val activeJob = rolePlayer.activeJob
    experienceRemaining(activeJob.level, activeJob.experience) match {
      case Some(data) if data > experience =>
        PlayerController.updatePlayerJob(rolePlayer, PlayerJob(activeJob.job, activeJob.level, activeJob.experience + experience))
      case Some(data) if data <= experience => LevelController.levelUp(rolePlayer, experience - data)
      case _ => rolePlayer
    }
  }
}
