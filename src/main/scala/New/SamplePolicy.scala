package New

import play.api.libs.json._

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait Rule {
  def performPolicy(e: Event): Unit

  protected def policy(): Unit = println("--->")
}

case object ALLOW extends Rule {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Allow Traffic")
    println()
    println()
  }
}

case object DEPLOY_DPI extends Rule {
  override def performPolicy(e: Event): Unit = {
//    execute action = Deploy DPI at ${e.resource_ID} ... ")
    policy()
    println(s"...Done")
    println("DPI Deployed Successfully")
    println()
  }
}

case object MIGRATE extends Rule {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Migrate ${e.resource_ID}")
    println()
    println()
  }
  //}
  //case object RE_DIRECT extends Rule {
  //  override def performPolicy(e: Event): Unit = {
  //    println(s"Action = No action required. Will do nothing")
  //    println()
  //    println()
  // }
}

case object DENY_SOURCE extends Rule {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Denying source for ${e.resource_ID} ... ")
    policy()
    println(s"...Done")
    println()
    println()
  }
}

case object CLOSE_APP extends Rule {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = Close Application ${e.resource_ID} ...")
    println()
    println()
  }
}

case object NO_ACTION extends Rule {
  override def performPolicy(e: Event): Unit = {
    println(s"Action = No action required. Will do nothing")
    println()
    println()
  }
}

case class Event(e_type: String, e_rate: Int, t_type: String, resource_ID: String, severity: String)

case class EventWithPolicy(event: Event, policy: Rule)

object Flooding extends App {
  implicit val eventReads: Reads[Event] = Json.reads[Event]

  val jsonAlertsStr: String = Source.fromFile("src/resources/alert.json").mkString
  val alertsValue: JsValue = Json.parse(jsonAlertsStr)

  val jsValueToEventObject: List[Event] = Try(Json.fromJson[List[Event]](alertsValue).get) match {
    case Failure(ex) => throw ex
    case Success(value) => value
  }

  def mapAlertsToPolicy(alertEvents: List[Event]): List[EventWithPolicy] = {
    alertEvents.map(e => {
      val rate = e.e_rate
      val severeness = e.severity
      if (rate > 500) {
        EventWithPolicy(e, DENY_SOURCE)
      } else if (rate > 100) {
        if (severeness == "moderate") EventWithPolicy(e, DEPLOY_DPI)
        else EventWithPolicy(e, NO_ACTION)
      } else {
        EventWithPolicy(e, NO_ACTION) //FIXME: Not sure what this should be.
      }
    })
  }

  mapAlertsToPolicy(jsValueToEventObject)
    .foreach((ewa: EventWithPolicy) => ewa.policy.performPolicy(ewa.event))
}

