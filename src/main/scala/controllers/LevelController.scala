package controllers

import models.{PlayerJob, RolePlayer}

/**
  * Created by james-forster on 28/10/16.
  */
object LevelController extends LevelController

trait LevelController {

  def levelUp(rolePlayer: RolePlayer, remainingExp: Int): RolePlayer = {
    val updatedJob = PlayerJob(rolePlayer.activeJob.job, rolePlayer.activeJob.level + 1, remainingExp)
    val leveledPlayer = PlayerController.updatePlayerJob(rolePlayer, updatedJob)
    val expToLevel = ExperienceController.maxExperience(leveledPlayer.activeJob.level)

    expToLevel match {
      case Some(value) if value <= remainingExp => levelUp(leveledPlayer, remainingExp - value)
      case _ => leveledPlayer
    }
  }
}
