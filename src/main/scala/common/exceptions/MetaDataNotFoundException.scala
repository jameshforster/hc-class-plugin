package common.exceptions

/**
  * Created by james-forster on 26/10/16.
  */

case class MetaDataNotFoundException() extends Exception

object MetaDataNotFoundException {
  def apply(message: String): MetaDataNotFoundException = new MetaDataNotFoundException {
    new Exception(message)
  }
}
