package New

import play.api.libs.json._

import scala.collection.immutable
import scala.io.Source

//class SamplePolicy

case class Event(e_type: String, e_rate: Int, t_type: String, resource_ID: String, severity: String)

object Flooding extends App {
  implicit val eventReads: Reads[Event] = Json.reads[Event]

  val filename = "src/main/scala/New/Jsonevent2.json"

  var eventsList = List.empty[Event]

  val jsonFromFile = Source.fromFile(filename).mkString
  val jsonStringToJsValue: JsValue = Json.parse(jsonFromFile)

  val jsValueToEventObject: List[Event] = Json.fromJson[List[Event]](jsonStringToJsValue).get

  def Condition(alert: Event): Unit = {
    if (alert.e_rate > 100) {
      println(s"Action = deploy DPI (${alert.resource_ID})")
    }
  }

  jsValueToEventObject.foreach((event: Event) => Condition(event))

//  def DenyAll(alert2: Event): Unit = {
//    if (alert2.e_rate > 500) {
//      println("Action = Deny Source")
//    }
//  }
}

