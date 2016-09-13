package common

/**
  * Created by james-forster on 13/09/16.
  */
object Maths {

  val negativeToZero: Int => Int = value => {
    if (value > 0) value
    else 0
  }
}
