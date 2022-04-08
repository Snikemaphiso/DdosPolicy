package ddos.main.controllers

import ddos.main.models.{Event, EventWithPolicy}
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