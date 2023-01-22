package models

import play.api.libs.json.Json.{format, parse}
import play.api.libs.json.OFormat

import scala.util.Try

case class Condition(flow: String)

case class Policy(name: String,
                  policyType: String,
                  target: String,
                  severity: String,
                  condition: Condition,
                  action: String) //TODO: Action needs to be mapped to the Action trait so we can have it as `action: Action`
extends DdosInputType with DdosJson {

  implicit val conditionFormat: OFormat[Condition] = format[Condition]
  implicit val policyFormat: OFormat[Policy] = format[Policy]

  override def getDdosInputFromJson(jsonString: String): Seq[Policy] =
    Try(parse(jsonString).validate[Seq[Policy]].getOrElse(Nil)).recover{ e => e.printStackTrace(); Nil}.get
}
