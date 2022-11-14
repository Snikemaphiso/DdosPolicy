package models

import play.api.libs.json.{Json, OFormat}

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
                )

object Event extends DdosJson {

  implicit val eventHeaderFormat: OFormat[EventHeader] = Json.format[EventHeader]
  implicit val eventTypeFormat: OFormat[EventType] = Json.format[EventType]
  implicit val targetFormat: OFormat[Target] = Json.format[Target]
  implicit val targetStateFormat: OFormat[TargetState] = Json.format[TargetState]
  implicit val targetLocationFormat: OFormat[TargetLocation] = Json.format[TargetLocation]
  implicit val consumptionFormat: OFormat[Consumption] = Json.format[Consumption]
  implicit val temporalBehaviourFormat: OFormat[TemporalBehaviour] = Json.format[TemporalBehaviour]
  implicit val eventCharacteristicsFormat: OFormat[EventCharacteristics] = Json.format[EventCharacteristics]

  implicit val eventFormat: OFormat[Event] = Json.format[Event]
}
