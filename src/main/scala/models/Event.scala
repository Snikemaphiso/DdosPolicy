package models

import play.api.libs.json.{Json, Reads}

case class Event(e_type: String, e_rate: Int, t_type: String, resource_ID: String, severity: String)

object Event extends DdosJson {
  implicit val eventRead: Reads[Event] = Json.reads[Event]
}
