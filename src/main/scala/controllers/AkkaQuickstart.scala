//#full-example
package controllers


import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.event.slf4j.Logger
import controllers.GreeterMain.SayHello

import java.util.UUID
import scala.util.Random

//#greeter-actor
object Greeter {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])

  final case class Greeted(whom: String, from: ActorRef[Greet])

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    context.log.info("Hello {}!", message.whom)
    //#greeter-send-messages
    message.replyTo ! Greeted(message.whom, context.self)
    //#greeter-send-messages
    //context.log.info("Hello {}!", message.whom)
    //message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }
}
//#greeter-actor

//#greeter-bot
object GreeterBot {

  def apply(): Behavior[Greeter.Greeted] = {
    Behaviors.receive { (context, message) =>
      //val n = greetingCounter + 1
      if (message.whom == "Charles") {
        context.log.info("Greeting Detected for {}", message.whom)
        Behaviors.stopped
      } else {
        //message.from ! Greeter.Greet(message.whom, context.self)
        context.log.info("Greeting Detected for {}", message.whom)
        Behaviors.stopped
      }
    }
  }
}
//#greeter-bot

//#greeter-main
object GreeterMain {

  final case class SayHello(name: String)

  def apply(): Behavior[SayHello] =
    Behaviors.setup { context: ActorContext[SayHello] =>
      //#create-actors
      val greeter = context.spawn(Greeter(), "greeter")
      //#create-actors

      Behaviors.receiveMessage { message: SayHello =>
        //#create-reply-actor-for-each-greeter
        val replyTo = context.spawn(GreeterBot(), message.name + "-" + UUID.randomUUID()) //Added randomUUID to the name as actor names have to be unique
        //#create-reply-actor-for-each-greeter
        greeter ! Greeter.Greet(message.name, replyTo)
        Behaviors.same
      }
    }
}
//#greeter-main

//#main-class
object AkkaQuickstart extends App {
  val log = Logger.apply("AkkaQuickStart")

  //#actor-system
  val greeterMain: ActorSystem[SayHello] = ActorSystem(GreeterMain(), "AkkaQuickStart")
  //#actor-system

  val namesList = List("John", "Charles", "Peter")

  def randomNum: Int = Random.nextInt(namesList.length)

  val max = 10
  var maxRuns = Random.nextInt(max) //Will choose a random number between 1 to the value of max, for each run

  log.info(s"Will say 'Hello', a total of [$maxRuns times] to any of [${namesList.mkString(", ")}]")

  while (maxRuns != 0) {
    val name = namesList(randomNum)
    greeterMain ! SayHello(name) //Remember, these calls are concurrent so greetings and replies may not the in the order that they were sent
    maxRuns = maxRuns - 1
  }
}
//#main-class
//#full-example