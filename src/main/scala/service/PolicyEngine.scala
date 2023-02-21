package service

import models._

object PolicyEngine {
  def isEventMatching(policy: Policy, event: Event) = {
    policy.target == event.target.target_type &&
      policy.severity == event.severity.getOrElse("") &&
      isConditionalMatching(policy, event)
  }

  private def isConditionalMatching(policy: Policy, event: Event) = {
    val conditionalString = policy.condition.flow
    val operator = conditionalString.takeWhile(!_.isDigit)
    val value = conditionalString.dropWhile(!_.isDigit).toInt

    //    println(policy.name + ",oper," + operator + ",value," + value)
    val x = event.event_xterics.consumption.consumption_rate
    operator match {
      case ">" => x > value
      case ">=" => x >= value
      case "<" => x < value
      case "<=" => x <= value
      case "==" => x == value
      case "!=" => x != value
      case _ => throw new IllegalArgumentException("Invalid operator in conditional string")
    }
  }

  def getActionObject(policy: Policy): Option[Action] = {
    policy.action match {
      case "ALLOW" => Some(ALLOW)
      case "DEPLOY_DPI" => Some(DEPLOY_DPI)
      case "MIGRATE" => Some(MIGRATE)
      case "DENY_SOURCE" => Some(DENY_SOURCE)
      case "CLOSE_APP" => Some(CLOSE_APP)
      case "NO_ACTION" => Some(NO_ACTION)
      case _ => Option.empty
    }
  }
}
