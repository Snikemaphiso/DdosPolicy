package models

import play.api.libs.json.Json.{format, parse}
import play.api.libs.json.OFormat
import spray.json.DefaultJsonProtocol.{jsonFormat2, jsonFormat3, jsonFormat4, jsonFormat7}
import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._
import scala.util.Try

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
                ) extends DdosInputType with DdosJson {

  implicit val eventHeaderFormat: OFormat[EventHeader] = format[EventHeader]
  implicit val eventTypeFormat: OFormat[EventType] = format[EventType]
  implicit val targetFormat: OFormat[Target] = format[Target]
  implicit val targetStateFormat: OFormat[TargetState] = format[TargetState]
  implicit val targetLocationFormat: OFormat[TargetLocation] = format[TargetLocation]
  implicit val consumptionFormat: OFormat[Consumption] = format[Consumption]
  implicit val temporalBehaviourFormat: OFormat[TemporalBehaviour] = format[TemporalBehaviour]
  implicit val eventCharacteristicsFormat: OFormat[EventCharacteristics] = format[EventCharacteristics]

  implicit val eventFormat: OFormat[Event] = format[Event]

  override def getDdosInputFromJson(jsonString: String): Seq[Event] =
    Try(parse(jsonString).validate[Seq[Event]].getOrElse(Nil)).recover{ e => e.printStackTrace(); Nil}.get
}
