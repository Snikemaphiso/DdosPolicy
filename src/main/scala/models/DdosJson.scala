package models

import play.api.libs.json.JsValue
import play.api.libs.json.Json.parse

import scala.io.Source
import scala.util.{Try, Using}

trait DdosJson {
  def getJsonString(jsonFileLocation: String): String = Using(Source.fromFile(jsonFileLocation))(_.mkString).fold(throw _, str => str)
  def getAsJSValue(jsonFileLocation: String): JsValue = parse(getJsonString(jsonFileLocation))

  def getEventFromJsonString(jsonString: String): Seq[Event] = Try(parse(jsonString).validate[List[Event]].getOrElse(Nil)).fold(throw _, e => e)
}
