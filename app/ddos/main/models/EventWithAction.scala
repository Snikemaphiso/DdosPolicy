package ddos.main.models

import ddos.main.controllers.Action
import play.api.libs.json.{Json, OFormat}

case class EventWithAction(event: Event, action: Action)

object EventWithAction {
  implicit val eventRead: OFormat[EventWithAction] = Json.format[EventWithAction]
}