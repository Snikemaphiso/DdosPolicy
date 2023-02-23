//#full-example
package controllers

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.http.scaladsl.Http
import controllers.Router._
import models._
import play.api.libs.json.Json.{prettyPrint, toJson}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object EventConsumer {

  def apply(): Behavior[ReceivedEvent] = Behaviors.receive { (context, receivedEvent: ReceivedEvent) =>
    context.log.info("Received DDOS event: \n{}", prettyPrint(toJson(receivedEvent.payload.toString)))
    receivedEvent.replyTo ! Recipient(receivedEvent.payload, context.self)
    Behaviors.same
  }

  final case class ReceivedEvent(payload: Event, replyTo: ActorRef[Recipient])

  final case class Recipient(payload: Event, from: ActorRef[ReceivedEvent])
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

        event.event_xterics.consumption.consumption_rate match {
          case rate: Int =>
            if (rate >= 100 && rate < 200) {
              val replyTo: ActorRef[EventConsumer.Recipient] = context.spawn(PolicyOneBot(), "Policy_One")
              context.log.info("Performing action for Policy One")
              eventConsumer ! EventConsumer.ReceivedEvent(event, replyTo)
              Behaviors.same
            }
          case rate: Int =>
            if (rate >= 200 && rate < 300) {
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

object DdosPolicyStart extends App with SprayJsonImplicits {
  implicit val ddosMainActor: ActorSystem[Event] = ActorSystem(DdosEventListener(), "DdosEventListener")
  implicit val ec: ExecutionContextExecutor = ddosMainActor.executionContext

  val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt(host, port).bind(route)

  println(s"Server now online. Please navigate to http://$host:$port/event\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => ddosMainActor.terminate()) // and shutdown when done
}