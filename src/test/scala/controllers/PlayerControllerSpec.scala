package controllers

import connectors.ServerConnector
import models.{Jobs, PlayerJob}
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{OptionValues, WordSpecLike}

/**
  * Created by james-forster on 13/09/16.
  */
class PlayerControllerSpec extends AnyRef with WordSpecLike with org.scalatest.Matchers with OptionValues with MockitoSugar {

  def setupTarget(getMetadataResult: Option[String]) = {

    val mockPlugin = mock[Plugin]
    val mockServerConnector = mock[ServerConnector]

    when(mockServerConnector.getPlayerMetaData(Matchers.any(), Matchers.any()))
      .thenReturn(getMetadataResult)

    new PlayerController {
      override val serverConnector: ServerConnector = mockServerConnector
      override val plugin: Plugin = mockPlugin
    }
  }

  "Calling getActivePlayerJob" should {

    "return a None when an error occurs" in {
      val target = setupTarget(None)
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe None
    }

    "return Jobless when no data is found" in {
      val target = setupTarget(Some(""))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Some(PlayerJob(Jobs.Jobless, 0, 0))
    }

    "return a Warrior Job when data is found" in {
      val target = setupTarget(Some("job=Warrior&level=2&experience=5"))
      val result = target.getActivePlayerJob(mock[Player])

      result shouldBe Some(PlayerJob(Jobs.Warrior, 2, 5))
    }
  }
}
