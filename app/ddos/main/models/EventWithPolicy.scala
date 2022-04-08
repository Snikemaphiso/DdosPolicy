package ddos.main.models

import ddos.main.controllers.Policy
import play.api.libs.json.{Json, OFormat}

case class EventWithPolicy(event: Event, policy: Policy)

object EventWithPolicy {
  implicit val eventRead: OFormat[EventWithPolicy] = Json.format[EventWithPolicy]
}