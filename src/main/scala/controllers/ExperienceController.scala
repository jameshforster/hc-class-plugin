package controllers

import java.math.MathContext

import common.Maths

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
}
