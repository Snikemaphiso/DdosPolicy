package ddos.main.controllers

import ddos.main.models.Event

trait Policy {
  def performPolicy(e: Event): Unit
  protected def policy(): Unit = println("--->")
}

case object ALLOW extends Policy {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Allow Traffic")
    println()
    println()
  }
}

case object DEPLOY_DPI extends Policy {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Deploying DPI at ${e.resource_ID} ... ")
    policy()
    println(s"...Done")
    println()
    println()
  }
}

case object MIGRATE extends Policy {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Migrate ${e.resource_ID}")
    println()
    println()
  }
}

case object DENY_SOURCE extends Policy {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Denying source for ${e.resource_ID} ... ")
    policy()
    println(s"...Done")
    println()
    println()
  }
}

case object CLOSE_APP extends Policy {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Close Application ${e.resource_ID} ...")
    println()
    println()
  }
}

case object NO_ACTION extends Policy {
  override def performPolicy(e: Event): Unit = {
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