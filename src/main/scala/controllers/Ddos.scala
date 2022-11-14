package controllers

import models.{Event, EventWithAction}
import play.api.libs.json.JsResult

import scala.util.Try

object Ddos extends App {

  val alertsFile = "app/resources/alert.json"
  val jsonAlerts: JsResult[List[Event]] = Event.getAsJSValue(alertsFile).validate[List[Event]]

  val events: List[Event] = Try(jsonAlerts.getOrElse(Nil)).fold(throw _, e => e)

  def mapAlertsToPolicy(alertEvents: List[Event]): List[EventWithAction] = { //TODO: Whole method to be rewritten so rules are fetched from `Policy`
    alertEvents.map(e => {
      val rate = e.event_xterics.consumption.consumption_rate
      val severity = e.severity
      if (rate > 500) {
        EventWithAction(e, DENY_SOURCE)
      } else if (rate > 100) {
        if (severity.isEmpty) EventWithAction(e, DEPLOY_DPI)
        else EventWithAction(e, NO_ACTION)
      } else {
        EventWithAction(e, NO_ACTION)
      }
    })
  }

  mapAlertsToPolicy(events)
    .foreach((ewa: EventWithAction) => ewa.action.performPAction(ewa.event))
}