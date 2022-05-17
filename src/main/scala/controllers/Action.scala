package controllers

import models.Event

abstract class Action {
  def performPAction(e: Event): Unit
  protected def actionName(): Unit = println("Action: " + this.getClass.getSimpleName)
}

case object ALLOW extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Allow Traffic")
    println()
    println()
  }
}

case object DEPLOY_DPI extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Deploying DPI at ${e.resource_ID} ... ")
    actionName()
    println(s"...Done")
    println()
    println()
  }
}

case object MIGRATE extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Migrate ${e.resource_ID}")
    println()
    println()
  }
}

case object DENY_SOURCE extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Denying source for ${e.resource_ID} ... ")
    actionName()
    println(s"...Done")
    println()
    println()
  }
}

case object CLOSE_APP extends Action {
  override def performPAction(e: Event): Unit = {
    println(s"Action = Close Application ${e.resource_ID} ...")
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