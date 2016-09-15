package common

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import org.mockito.Matchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{OptionValues, WordSpecLike}
import org.mockito.Mockito._

/**
  * Created by jamez on 15/09/2016.
  */
class AppConfigSpec extends AnyRef with WordSpecLike with org.scalatest.Matchers with OptionValues with MockitoSugar{

  def configMock(mockBoolean: Boolean, mockString: String): Plugin = {

    val mockPlugin = mock[Plugin]
    val mockConfig = mock[FileConfiguration]

    when(mockConfig.getBoolean(Matchers.any()))
      .thenReturn(mockBoolean)

    when(mockConfig.getString(Matchers.any()))
      .thenReturn(mockString)

    when(mockPlugin.getConfig)
      .thenReturn(mockConfig)

    mockPlugin
  }

  "Setting the config values" should {
    val mockPlugin = configMock(true, "test")
    val target = AppConfig(mockPlugin)

    "set a boolean for storageEnabled" in {
      target.storageEnabled shouldBe true
    }

    "set a boolean for storageEnabled" in {
      target.databaseEnabled shouldBe true
    }

    "set a string for databaseHost" in {
      target.databaseHost shouldBe "test"
    }

    "set a string for databasePort" in {
      target.databasePort shouldBe "test"
    }
  }
}
