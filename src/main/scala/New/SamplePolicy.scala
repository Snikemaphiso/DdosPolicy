package New

import play.api.libs.json._

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait Rule {
  def performAction(e: Event): Unit
  protected def action(): Unit = println(" ")
}
case object DEPLOY_DPI extends Rule {
  override def performAction(e: Event): Unit = {
    println(s"Action = Deploying DPI for ${e.resource_ID} ... ")
    action()
    println(s"...Done")
    println()
    println()
  }
}
case object DENY_SOURCE extends Rule {
  override def performAction(e: Event): Unit = {
    println(s"Action = Denying source for ${e.resource_ID} ... ")
    action()
    println(s"...Done")
    println()
    println()
  }
}
case object NO_ACTION extends Rule {
  override def performAction(e: Event): Unit = {
    println(s"Action = No action required. Will do nothing")
    println()
    println()
  }
}


case class Event(e_type: String, e_rate: Int, t_type: String, resource_ID: String, severity: String)
case class EventWithAction(event: Event, action: Rule)

object Flooding extends App {
  implicit val eventReads: Reads[Event] = Json.reads[Event]

  val jsonAlertsStr: String = Source.fromFile("src/resources/alert.json").mkString
  val alertsValue: JsValue = Json.parse(jsonAlertsStr)

  val jsValueToEventObject: List[Event] = Try(Json.fromJson[List[Event]](alertsValue).get) match {
    case Failure(ex) => throw ex
    case Success(value) => value
  }

  def mapAlertsToActions(alertEvents: List[Event]): List[EventWithAction] = {
    alertEvents.map( e => {
      val rate = e.e_rate
      if (rate > 500) EventWithAction(e, DENY_SOURCE)
      else if (rate > 100) EventWithAction(e, DEPLOY_DPI)
      else EventWithAction(e, NO_ACTION)
    }
    )
  }

  mapAlertsToActions(jsValueToEventObject)
    .foreach((ewa: EventWithAction) => ewa.action.performAction(ewa.event))
}

