package connectors

import org.bukkit.entity.Player
import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.Plugin
import org.scalatest.mock.MockitoSugar
import org.scalatest.{OptionValues, WordSpecLike}
import org.mockito.Matchers
import org.mockito.Mockito._
import collection.JavaConverters._
import scala.util.{Failure, Success}
import common.exceptions.MetaDataNotFoundException

/**
  * Created by james-forster on 12/09/16.
  */
class ServerConnectorSpec extends AnyRef with WordSpecLike with org.scalatest.Matchers with OptionValues with MockitoSugar {

  val illegalArgumentException = new IllegalArgumentException

  def setupConnector(): ServerConnector = {

    val mockPlugin = mock[Plugin]

    new ServerConnector {
      lazy val plugin: Plugin = mockPlugin
    }
  }

  def setupPlayer(fails: Boolean, plugin:Plugin, data: String = ""): Player = {

    val mockPlayer = mock[Player]
    val mockMetaDataValue = mock[MetadataValue]

    when(mockMetaDataValue.getOwningPlugin)
      .thenReturn(plugin)

    when(mockMetaDataValue.asString())
      .thenReturn(data)

    if (fails) {
      when(mockPlayer.setMetadata(Matchers.any(), Matchers.any()))
        .thenThrow(illegalArgumentException)
      when(mockPlayer.getMetadata(Matchers.any()))
        .thenThrow(illegalArgumentException)
      when(mockPlayer.removeMetadata(Matchers.any(), Matchers.any()))
        .thenThrow(illegalArgumentException)
    } else {
      if (data.equals("")) {
        when(mockPlayer.getMetadata(Matchers.any()))
          .thenReturn(List[MetadataValue]().asJava)
      }
      else {
        when(mockPlayer.getMetadata(Matchers.any()))
          .thenReturn(List(mockMetaDataValue).asJava)
      }
    }

    mockPlayer
  }

  "Calling the setPlayerMetaData method" should {

    "return a Failure when failing" in {
      val connector = setupConnector()
      val player = setupPlayer(true, connector.plugin)
      val result = connector.setPlayerMetaData(player, "testKey", "testData")

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Success when successful" in {
      val connector = setupConnector()
      val player = setupPlayer(false, connector.plugin)
      val result = connector.setPlayerMetaData(player, "testKey", "testData")

      result shouldBe Success()
    }
  }

  "Calling the getPlayerMetaData method" should {

    "return a Failure when failing" in {
      val connector = setupConnector()
      val player = setupPlayer(true, connector.plugin)
      val result = connector.getPlayerMetaData(player, "testKey")

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Failure when no data is found" in {
      val connector = setupConnector()
      val player = setupPlayer(false, connector.plugin)
      val result = connector.getPlayerMetaData(player, "testKey")

      result shouldBe Failure(MetaDataNotFoundException())
    }

    "return a Success when successful" in {
      val connector = setupConnector()
      val player = setupPlayer(false, connector.plugin, "value")
      val result = connector.getPlayerMetaData(player, "testKey")

      result shouldBe Success("value")
    }
  }

  "Calling the removePlayerMetaData method" should {

    "return a Failure when failing" in {
      val connector = setupConnector()
      val player = setupPlayer(true, connector.plugin)
      val result = connector.removePlayerMetaData(player, "testKey")

      result shouldBe Failure(illegalArgumentException)
    }

    "return a Failure(MetaDataNotFoundException) when no value is found" in {
      val connector = setupConnector()
      val player = setupPlayer(false, connector.plugin)
      val result = connector.removePlayerMetaData(player, "testKey")

      result shouldBe Failure(MetaDataNotFoundException())
    }

    "return a Success when a value is found and deleted" in {
      val connector = setupConnector()
      val player = setupPlayer(false, connector.plugin, "value")
      val result = connector.removePlayerMetaData(player, "testKey")

      result shouldBe Success()
    }
  }
}
