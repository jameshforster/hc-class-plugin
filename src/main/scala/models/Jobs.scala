package models

/**
  * Created by james-forster on 31/08/16.
  */

object Jobs {
  //each job must be created as an object here that extends the Job trait
  object Jobless extends Job
  object Warrior extends Job
  object Rogue extends Job
  object Wizard extends Job

  //each job object must be added to this sequence to be valid and usable
  val jobsList: Seq[Job] = {
    Seq(Jobless, Warrior, Rogue, Wizard)
  }
}

trait Job {

  val jobName: String = this.getClass.getSimpleName.stripSuffix("$")

}
