//#full-example
package controllers

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.event.slf4j.Logger
import controllers.AlertMain.SayEvent

import java.util.UUID
import scala.util.Random

//#greeter-actor
object PolicyModel {
  final case class Alert(whom: String, replyTo: ActorRef[Listener])

  final case class Listener(whom: String, from: ActorRef[Alert])

  def apply(): Behavior[Alert] = Behaviors.receive { (context, message) =>
    context.log.info("Event Severity: {}!", message.whom)
    //#greeter-send-messages
    message.replyTo ! Listener(message.whom, context.self)
    //#greeter-send-messages
    //context.log.info("Hello {}!", message.whom)
    //message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }
}
//#greeter-actor

//#greeter-bot
object AlertBot {

  def apply(): Behavior[PolicyModel.Listener] = {

    val logMessagePrefix = "Event listener Detected event with Severity: {}. Policy Triggered:"

    Behaviors.receive { (context, message) =>
      message.whom match {
        case "Low" =>
          context.log.info(s"$logMessagePrefix Deploy DPI", message.whom)
          Behaviors.stopped
        case "Severe" =>
          context.log.info(s"$logMessagePrefix Deny Source", message.whom)
          Behaviors.stopped
        case _ => // So when you need to handle another message severity (e.g. Medium), just create a new case before this default case
          context.log.info("Unsupported message type: {}", message.whom)
          Behaviors.stopped
      }
    }
  }
}

//#greeter-bot

//#greeter-main
object AlertMain {

  final case class SayEvent(name: String)

  def apply(): Behavior[SayEvent] =
    Behaviors.setup { context: ActorContext[SayEvent] =>
      //#create-actors
      val policyModel = context.spawn(PolicyModel(), "listener")
      //#create-actors

      Behaviors.receiveMessage { message: SayEvent =>
        //#create-reply-actor-for-each-greeter
        val replyTo = context.spawn(AlertBot(), message.name + "-" + UUID.randomUUID()) //Added a random number to the name as actor names have to be unique
        //#create-reply-actor-for-each-greeter
        policyModel ! PolicyModel.Alert(message.name, replyTo)
        Behaviors.same
      }
    }
}
//#greeter-main

//#main-class
object AkkaQuickstart extends App {
  val log = Logger.apply("AkkaQuickStart")

  //#actor-system
  val alertMain: ActorSystem[SayEvent] = ActorSystem(AlertMain(), "AkkaQuickStart")
  //#actor-system

  val namesList = List("Severe", "Low", "Medium")

  def randomNum: Int = Random.nextInt(namesList.length)

  val max = 10
  var maxRuns = Random.nextInt(max) //Will choose a random number between 1 to the value of max, for each run

  log.info(s"Reporting 'Event severity', a total of [$maxRuns times] with any of [${namesList.mkString(", ")}]")

  while (maxRuns != 0) {
    val name = namesList(randomNum)
    alertMain ! SayEvent(name) //Remember, these calls are concurrent so alert and response may not be in the order that they were sent
    maxRuns = maxRuns - 1
  }
}
//#main-class
//#full-example
