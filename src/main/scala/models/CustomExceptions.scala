package models

/**
  * Created by james-forster on 19/10/16.
  */
object CustomExceptions {

  case class MetaDataNotFoundException() extends Exception

  object MetaDataNotFoundException {
    def apply(message: String): MetaDataNotFoundException = new MetaDataNotFoundException {
      new Exception(message)
    }
  }
}
