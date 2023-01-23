package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import play.api.libs.json.Json.format
import play.api.libs.json.OFormat
import spray.json.{DefaultJsonProtocol, NullOptions, RootJsonFormat}


trait DdosInputType

object DdosPlayJsonImplicits {
  implicit val eventHeaderFormat: OFormat[EventHeader] = format[EventHeader]
  implicit val eventTypeFormat: OFormat[EventType] = format[EventType]
  implicit val targetFormat: OFormat[Target] = format[Target]
  implicit val targetStateFormat: OFormat[TargetState] = format[TargetState]
  implicit val targetLocationFormat: OFormat[TargetLocation] = format[TargetLocation]
  implicit val consumptionFormat: OFormat[Consumption] = format[Consumption]
  implicit val temporalBehaviourFormat: OFormat[TemporalBehaviour] = format[TemporalBehaviour]
  implicit val eventCharacteristicsFormat: OFormat[EventCharacteristics] = format[EventCharacteristics]
  implicit val eventFormat: OFormat[Event] = format[Event]

  implicit val conditionFormat: OFormat[Condition] = format[Condition]
  implicit val policyFormat: OFormat[Policy] = format[Policy]
}

trait SprayJsonImplicits extends DefaultJsonProtocol with SprayJsonSupport with NullOptions {
  implicit val eventHeaderSprayFormat: RootJsonFormat[EventHeader] = jsonFormat4(EventHeader)
  implicit val eventTypeSprayFormat: RootJsonFormat[EventType] = jsonFormat3(EventType)
  implicit val targetSprayFormat: RootJsonFormat[Target] = jsonFormat2(Target)
  implicit val targetStateSprayFormat: RootJsonFormat[TargetState] = jsonFormat2(TargetState)
  implicit val consumptionSprayFormat: RootJsonFormat[Consumption] = jsonFormat2(Consumption)
  implicit val targetLocationSprayFormat: RootJsonFormat[TargetLocation] = jsonFormat2(TargetLocation)
  implicit val temporalBehaviourSprayFormat: RootJsonFormat[TemporalBehaviour] = jsonFormat2(TemporalBehaviour)
  implicit val eventCharacteristicsSprayFormat: RootJsonFormat[EventCharacteristics] = jsonFormat2(EventCharacteristics)
  implicit val eventSprayFormat: RootJsonFormat[Event] = jsonFormat7(Event)

  implicit val conditionSprayFormat: RootJsonFormat[Condition] = jsonFormat1(Condition)
  implicit val policySprayFormat: RootJsonFormat[Policy] = jsonFormat6(Policy)
}