package controllers

import common.UnitSpec
import models.{Jobs, PlayerJob, RolePlayer}
import org.bukkit.entity.Player
import org.scalatest.mock.MockitoSugar

/**
  * Created by james-forster on 31/10/16.
  */
class LevelControllerSpec extends UnitSpec with MockitoSugar {

  "Calling level up" should {
    val mockPlayer = mock[Player]

    "return a player with level increased by one" in {
      val activeJob = PlayerJob(Jobs.Rogue, 2, 1)
      val finalJob = PlayerJob(Jobs.Rogue, 3, 5)
      val rolePlayer = RolePlayer(mockPlayer, activeJob, Seq(activeJob))
      val result = LevelController.levelUp(rolePlayer, 5)

      result shouldBe RolePlayer(mockPlayer, finalJob, Seq(finalJob))
    }

    "return a player with level increased by two with enough exp" in {
      val activeJob = PlayerJob(Jobs.Rogue, 1, 0)
      val finalJob = PlayerJob(Jobs.Rogue, 3, 0)
      val rolePlayer = RolePlayer(mockPlayer, activeJob, Seq(activeJob))
      val result = LevelController.levelUp(rolePlayer, 15)

      result shouldBe RolePlayer(mockPlayer, finalJob, Seq(finalJob))
    }
  }
}
