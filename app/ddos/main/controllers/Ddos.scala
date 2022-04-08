package ddos.main.controllers

import ddos.main.models.{Event, EventWithAction}
import play.api.libs.json.{JsValue, Json}

import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object Ddos extends App {

  val jsonAlertsStr: Try[String] = Using(Source.fromFile("app/resources/alert.json"))(_.mkString)
  val alertsValue: JsValue = jsonAlertsStr.toEither.fold(t => throw t, str => Json.parse(str))

  val jsValueToEventObject: List[Event] = Try(Json.fromJson[List[Event]](alertsValue).getOrElse(Nil)) match {
    case Failure(ex) => throw ex
    case Success(value) => value
  }

  def mapAlertsToPolicy(alertEvents: List[Event]): List[EventWithAction] = { //TODO: Whole method to be rewritten tso rules are fetched from `Policy`
    alertEvents.map(e => {
      val rate = e.e_rate
      val severeness = e.severity
      if (rate > 500) {
        EventWithAction(e, DENY_SOURCE)
      } else if (rate > 100) {
        if (severeness == "moderate") EventWithAction(e, DEPLOY_DPI)
        else EventWithAction(e, NO_ACTION)
      } else {
        EventWithAction(e, NO_ACTION)
      }
    })
  }

  mapAlertsToPolicy(jsValueToEventObject)
    .foreach((ewa: EventWithAction) => ewa.action.performPAction(ewa.event))
}