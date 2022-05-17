package models

import models.DdosJson
import play.api.libs.json.{Json, OFormat, Reads}

case class Condition(flow: String)
case object Condition {
  implicit val conditionFormat: OFormat[Condition] = Json.format[Condition]
}

case class Policy(name: String,
                  policyType: String,
                  target: String,
                  severity: String,
                  condition: Condition,
                  action: String) //TODO: Action needs to be mapped to the Action trait so we can have it as `action: Action`
case object Policy extends DdosJson {
  implicit val policyFormat: Reads[Policy] = Json.reads[Policy]
}
