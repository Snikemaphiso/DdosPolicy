//#full-example
package controllers

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import models.{DdosJson, Event}
import play.api.libs.json.Json.{toJson, prettyPrint}

object EventConsumer {
  final case class ReceivedEvent(payload: Event, replyTo: ActorRef[Recipient])
  final case class Recipient(payload: Event, from: ActorRef[ReceivedEvent])

  def apply(): Behavior[ReceivedEvent] = Behaviors.receive { (context, receivedEvent) =>
    context.log.info("Received DDOS event: \n{}", prettyPrint(toJson(receivedEvent.payload)))
    receivedEvent.replyTo ! Recipient(receivedEvent.payload, context.self)
    Behaviors.same
  }
}

object PolicyOneBot {

  def apply(): Behavior[EventConsumer.Recipient] =
    Behaviors.receive { (context, message: EventConsumer.Recipient) =>
      val event = message.payload

      def performAction(action: Action, severity: String = ""): Unit = {
        context.log.info(
          "Performing action for event type [{}] at Resource [{}] with Severity [{}]...",
          event.event_type.attack_class,
          event.target.target_type,
          severity
        )
        context.log.info(s"Performing Action of type: ${action.toString}")
        context.log.info("Done")
      }

      event.severity match {
        case Some(severity: String) => severity match {
          case "low" => performAction(ALLOW, severity)
          case "medium" => performAction(DENY_SOURCE, severity)
          case _ => context.log.info(s"Unknown severity: [$severity]")
        }
        case None =>
          context.log.info(s"No severity for event [${event.event_header.report_ID}]")
          performAction(DENY_SOURCE)
      }

      Behaviors.stopped
    }
}

object PolicyTwoBot {

  def apply(): Behavior[EventConsumer.Recipient] =
    Behaviors.receive { (context, message: EventConsumer.Recipient) =>
      context.log.info(
        "Performing action for event type [{}] at Resource [{}] with Severity [{}]...",
        message.payload.event_type.attack_class,
        message.payload.target.target_type,
        message.payload.severity.getOrElse("")
      )
      // TODO:
      //  Need to perform some action.
      //  We need to change payload from Event to Event with action,
      //  then we call `message.payload.action.performAction`
      context.log.info("Done")

      Behaviors.stopped
    }
}

object DdosEventListener {

  def apply(): Behavior[Event] =
    Behaviors.setup { context: ActorContext[Event] =>
      val eventConsumer = context.spawn(EventConsumer(), "EVENT_CONSUMER")

      Behaviors.receiveMessage { event: Event =>

//        If (event.event_xterics.consumption.consumption_rate >= 100 && event.severity="High"){}
        event.event_xterics.consumption.consumption_rate match {
          case rate: Int =>
            if (rate >= 100 && rate < 200) {
              val replyTo: ActorRef[EventConsumer.Recipient] = context.spawn(PolicyOneBot(), "Policy_One")
              context.log.info("Performing action for Policy One")
              eventConsumer ! EventConsumer.ReceivedEvent(event, replyTo)
              Behaviors.same
            }
//          case rate: Int =>
//            if (rate >= 200 && rate < 300 ) {
//              val replyTo: ActorRef[EventConsumer.Recipient] = context.spawn(PolicyTwoBot(), "Policy_Two")
//              context.log.info("Performing action for Policy Two")
//              eventConsumer ! EventConsumer.ReceivedEvent(event, replyTo)
//              Behaviors.same
//            }
          case _ => Behaviors.same
        }
        Behaviors.same
      }
    }
}

object DdosEventListener2 {

  def apply(): Behavior[Event] =
    Behaviors.setup { context: ActorContext[Event] =>
      val eventConsumer = context.spawn(EventConsumer(), "EVENT_CONSUMER")

      Behaviors.receiveMessage { event: Event =>

        //        If (event.event_xterics.consumption.consumption_rate >= 100 && event.severity="High"){}
        event.event_xterics.consumption.consumption_rate match {
//          case rate: Int =>
//            if (rate >= 100 && rate < 200) {
//              val replyTo: ActorRef[EventConsumer.Recipient] = context.spawn(PolicyOneBot(), "Policy_One")
//              context.log.info("Performing action for Policy One")
//              eventConsumer ! EventConsumer.ReceivedEvent(event, replyTo)
//              Behaviors.same
//            }
          case rate: Int =>
            if (rate >= 200 && rate < 300 ) {
              val replyTo: ActorRef[EventConsumer.Recipient] = context.spawn(PolicyTwoBot(), "Policy_Two")
              context.log.info("Performing action for Policy Two")
              eventConsumer ! EventConsumer.ReceivedEvent(event, replyTo)
              Behaviors.same
            }
          case _ => Behaviors.same
        }
        Behaviors.same
      }
    }
}
object DdosPolicyStart extends App with DdosJson {
  val ddosPolicyMain: ActorSystem[Event] = ActorSystem(DdosEventListener (),"DdosEventListener")

  val events: Seq[Event] = getEventFromJsonString(args.mkString)

  if (events.isEmpty) println("No passed args or args are not in the expected format")
  else for (event <- events) yield ddosPolicyMain ! event

}