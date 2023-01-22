package models

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

trait DdosInputType {

  //implicits to enable reads for Events from akka-http calls
  implicit val eventHeaderSprayFormat: RootJsonFormat[EventHeader] = jsonFormat4(EventHeader)
  implicit val eventTypeSprayFormat: RootJsonFormat[EventType] = jsonFormat3(EventType)
  implicit val targetSprayFormat: RootJsonFormat[Target] = jsonFormat2(Target)
  implicit val targetStateSprayFormat: RootJsonFormat[TargetState] = jsonFormat2(TargetState)
  implicit val consumptionSprayFormat: RootJsonFormat[Consumption] = jsonFormat2(Consumption)
  implicit val targetLocationSprayFormat: RootJsonFormat[TargetLocation] = jsonFormat2(TargetLocation)
  implicit val temporalBehaviourSprayFormat: RootJsonFormat[TemporalBehaviour] = jsonFormat2(TemporalBehaviour)
  implicit val eventCharacteristicsSprayFormat: RootJsonFormat[EventCharacteristics] = jsonFormat2(EventCharacteristics)
  implicit def eventSprayFormat: RootJsonFormat[Event] = jsonFormat7(Event.apply)

  //implicits to enable reads for Policy from akka-http calls
  implicit val conditionSprayFormat: RootJsonFormat[Condition] = jsonFormat1(Condition)
  implicit def policySprayFormat: RootJsonFormat[Policy] = jsonFormat6(Policy)
}
