package controllers

import common.UnitSpec
import models.{Jobs, PlayerJob, RolePlayer}
import org.bukkit.entity.Player
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, OptionValues, WordSpecLike}

/**
  * Created by jamez on 13/09/2016.
  */
class ExperienceControllerSpec extends UnitSpec with MockitoSugar {

  "Calling maxExperience" should {

    "return a 10 with a level of 1" in {
      val result = ExperienceController.maxExperience(1)
      result shouldBe Some(10)
    }

    "return a 15 with a level of 2" in {
      val result = ExperienceController.maxExperience(2)
      result shouldBe Some(15)
    }

    "return a 51 with a level of 5" in {
      val result = ExperienceController.maxExperience(5)
      result shouldBe Some(51)
    }

    "return a 260 with a level of 9" in {
      val result = ExperienceController.maxExperience(9)
      result shouldBe Some(260)
    }

    "return a 15000 with a level of 19" in {
      val result = ExperienceController.maxExperience(19)
      result shouldBe Some(15000)
    }

    "return a None with a level of 0" in {
      val result = ExperienceController.maxExperience(0)
      result shouldBe None
    }

    "return a None at max level" in {
      val result = ExperienceController.maxExperience(20)
      result shouldBe None
    }
  }

  "Calling experience remaining" should {

    "return a None with a level of 0" in {
      val result = ExperienceController.experienceRemaining(0, 0)
      result shouldBe None
    }

    "return a None at max level" in {
      val result = ExperienceController.experienceRemaining(20, 0)
      result shouldBe None
    }

    "return a value equal to max exp at 0" in {
      val result = ExperienceController.experienceRemaining(9, 0)
      result shouldBe Some(260)
    }

    "return a value of 10 at level 1" in {
      val result = ExperienceController.experienceRemaining(1, 0)
      result shouldBe Some(10)
    }

    "return a value of 15 at level 2" in {
      val result = ExperienceController.experienceRemaining(2, 0)
      result shouldBe Some(15)
    }

    "return a value equal to 25 at level 5 with 26 experience" in {
      val result = ExperienceController.experienceRemaining(5, 26)
      result shouldBe Some(25)
    }

    "return a value equal to 10000 at level 19 with 5000 experience" in {
      val result = ExperienceController.experienceRemaining(19, 5000)
      result shouldBe Some(10000)
    }
  }

  "Calling gainExperience" should {

    "only update the experience value when not enough to level up the player" in {
      val initialJob = PlayerJob(Jobs.Rogue, 5, 20)
      val endJob = PlayerJob(Jobs.Rogue, 5, 25)
      val mockPlayer = mock[Player]
      val player = RolePlayer(mockPlayer, initialJob, Seq(initialJob))
      val result = ExperienceController.gainExperience(player, 5)

      result shouldBe RolePlayer(mockPlayer, endJob, Seq(endJob))
    }

    "update only the active job when multiple jobs are listed" in {
      val initialJob = PlayerJob(Jobs.Rogue, 5, 20)
      val extraJob = PlayerJob(Jobs.Wizard, 2, 1)
      val endJob = PlayerJob(Jobs.Rogue, 5, 30)
      val mockPlayer = mock[Player]
      val player = RolePlayer(mockPlayer, initialJob, Seq(initialJob, extraJob))
      val result = ExperienceController.gainExperience(player, 10)

      result shouldBe RolePlayer(mockPlayer, endJob, Seq(endJob, extraJob))
    }

    "update the experience and level of the player when there exactly enough exp to level up" in {
      val initialJob = PlayerJob(Jobs.Rogue, 1, 0)
      val endJob = PlayerJob(Jobs.Rogue, 2, 0)
      val mockPlayer = mock[Player]
      val player = RolePlayer(mockPlayer, initialJob, Seq(initialJob))
      val result = ExperienceController.gainExperience(player, 10)

      result shouldBe RolePlayer(mockPlayer, endJob, Seq(endJob))
    }

    "update the experience and level of the player when there is exp left over after levelling up" in {
      val initialJob = PlayerJob(Jobs.Rogue, 1, 0)
      val endJob = PlayerJob(Jobs.Rogue, 2, 8)
      val mockPlayer = mock[Player]
      val player = RolePlayer(mockPlayer, initialJob, Seq(initialJob))
      val result = ExperienceController.gainExperience(player, 18)

      result shouldBe RolePlayer(mockPlayer, endJob, Seq(endJob))
    }

    "update the level multiple times when there is enough experience left over after levelling up" in {
      val initialJob = PlayerJob(Jobs.Rogue, 1, 0)
      val endJob = PlayerJob(Jobs.Rogue, 3, 2)
      val mockPlayer = mock[Player]
      val player = RolePlayer(mockPlayer, initialJob, Seq(initialJob))
      val result = ExperienceController.gainExperience(player, 27)

      result shouldBe RolePlayer(mockPlayer, endJob, Seq(endJob))
    }
  }
}
