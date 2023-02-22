package models

trait Action {
  def performPAction(e: Event): Unit
  protected def actionName(): Unit = println("Action: " + this.toString)
}

case object ALLOW extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Allow Traffic for ${e.target.target_ID} ${if(e.target_location.IP.isDefined) "at IP address: " + e.target_location.IP.get}")
    println()
    println()
  }
}

case object DEPLOY_DPI extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Deploying DPI at ${e.target.target_type} & ${e.target_location}... ")
    actionName()
    println(s"...Done")
    println()
    println()
  }
}

case object MIGRATE extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Migrate from ${e.target_location.location} to a new Virtual Location")
    println()
    println()
  }
}

case object DENY_SOURCE extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Denying source for ${e.event_type.attack_class} ... ")
    actionName()
    println(s"...Done")
    println()
    println()
  }
}

case object CLOSE_APP extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Close Application ${e.target.target_type} ...")
    println()
    println()
  }
}

case object NO_ACTION extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = No action required. Will do nothing")
    println()
    println()
  }
}

//case object RE_DIRECT extends Rule {
//  override def performPolicy(e: Event): Unit = {
//    println(s"Action = No action required. Will do nothing")
//    println()
//    println()
// }