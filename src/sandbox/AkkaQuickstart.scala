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
    context.log.info("Event Severity: {}!", message.whom)  // how to change context.log.info to automated values/fields from json file
    //context.log.info("Event characteristics: {}!", message.New)
    //#greeter-send-messages
    message.replyTo ! Listener(message.whom, context.self)
   // message.replyTo ! Listener(message.New, context.self)
    //#greeter-send-messages
    //context.log.info("Hello {}!", message.whom)
    //message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }
}
//#greeter-actor

//#greeter-bot
object Policy1 {

  def apply(): Behavior[PolicyModel.Listener] = {
    Behaviors.receive { (context, message) =>
      //val n = greetingCounter + 1
      if (message.whom == "Severe") {
        if (message.whom == "> 500mbs") {        // attempting multiple if condition checks for message
          context.log.info("Event listener 1 Detected with Severity: {}. Action: Deny Source", message.whom)
        }
          Behaviors.stopped
        }
      else {
        ////          message.from ! Greeter.Greet(message.whom, context.self)
        //          context.log.info("Event Detected with Severity: {}. Policy DPI Triggered", message.whom)
        Behaviors.stopped

      }
    }
  }
}

object Policy2 {   //should be able to add 2 more bots as policies with different if checks of the values from json

  def apply(): Behavior[PolicyModel.Listener] = {
    Behaviors.receive { (context, message) =>
      //val n = greetingCounter + 1
      if (message.whom == "Low") {
        if (message.whom == "< 500mbps") {  //eg
          context.log.info("Event listener 2 Detected event with Severity: {}. Action: Deploy DPI", message.whom, message.whom)  // should point to action.scala file
        }
        Behaviors.stopped
        }
        else {
          ////          message.from ! Greeter.Greet(message.whom, context.self)
          //        context.log.info("Event Detected with Severity: {}. Policy DPI Triggered", message.whom)
          Behaviors.stopped

        }
    }
  }
}

//object Policy3 {
//
//  def apply(): Behavior[PolicyModel.Listener] = {
//    Behaviors.receive { (context, message) =>
//      //val n = greetingCounter + 1
//      if (message.whom == "Medium") {
//        context.log.info("Event listener 3 Detected with Severity: {}. Policy DPI Triggered", message.whom)
//        Behaviors.stopped
//      }
//
//      else {
//        ////          message.from ! Greeter.Greet(message.whom, context.self)
//        //        context.log.info("Event Detected with Severity: {}. Policy DPI Triggered", message.whom)
//        Behaviors.stopped
//
//      }
//    }
//  }
//}
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
        val replyTo = context.spawn(Policy1(), message.name + "-" + UUID.randomUUID()) //Added randomUUID to the name as actor names have to be unique
        val replyTo2 = context.spawn(Policy2(), message.name + "-" + UUID.randomUUID())
        //#create-reply-actor-for-each-greeter
        policyModel ! PolicyModel.Alert(message.name, replyTo)
        policyModel ! PolicyModel.Alert(message.name, replyTo2)
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

  val namesList = List("Severe", "Low") // can we make values random auto inputs
  def randomNum: Int = Random.nextInt(namesList.length)

  val max = 5
  var maxRuns = Random.nextInt(max) //Will choose a random number between 1 to the value of max, for each run

  log.info(s"Reporting 'Event', a total of [$maxRuns times] with any of [${namesList.mkString(", ")}]")

  while (maxRuns != 0) {
    val name = namesList(randomNum)
    alertMain ! SayEvent(name) //Remember, these calls are concurrent so alert and response may not be in the order that they were sent
    maxRuns = maxRuns - 1
  }
}
//#main-class
//#full-example
