package models

import play.api.libs.json.Json.parse
import scala.util.Try
import models.DdosPlayJsonImplicits._

case class EventHeader(report_ID: Int, reporter_ID: Option[Int], time_stamp: Option[Long], related_report_ID: Option[Int])

case class EventType(attack_class: String, attack_types: Option[String], attack_granularity: Option[String])

case class Target(target_type: String, target_ID: Int)

case class TargetState(significance: Option[String], configuration: Option[String])

case class TargetLocation(location: String, IP: Option[Int])

case class EventCharacteristics(consumption: Consumption, temporal_behaviour: Option[TemporalBehaviour])

case class Consumption(
                        consumption_rate: Int, //Value in megabytes
                        consumption_type: Option[String]
                      )

case class TemporalBehaviour(start_time: Long, end_time: Option[Long])

case class Event(
                  event_header: EventHeader,
                  event_type: EventType,
                  target: Target,
                  target_state: Option[TargetState],
                  target_location: TargetLocation,
                  severity: Option[String],
                  event_xterics: EventCharacteristics
                ) extends DdosJson {

  override def getDdosInputFromJson(jsonString: String): Seq[Event] =
    Try(parse(jsonString).validate[Seq[Event]].getOrElse(Nil)).recover{ e => e.printStackTrace(); Nil}.get
}
