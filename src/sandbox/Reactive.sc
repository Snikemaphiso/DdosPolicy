//class Signal(initVal: Int) {
//  private var curVal = initVal
//  def apply(): Int = curVal
//  protected def update(x: Int): Unit = curVal = x
//}
//
//class Var(initVal:Int) extends Signal(initVal: Int) {
//  override def update(x: Int): Unit = super.update(x)
//}
//
//
//// Companion objects to enable instance creation without 'new' keyword
//object Signal { def apply(initVal: Int) = new Signal(initVal) }
//object Var { def apply(initVal: Int) = new Var(initVal) }
//
//class Var(initVal:Int) {
//  private val curVal = initVal
//  def apply(): Int = curVal
//}
//object Var {
//  def apply(initVal:Int) = new Var(initVal)
//}
//class Bandwidth {
//
//  var num = Var(0)
//
//  def Usage(x: Int): Unit = {
//    val curVal = num
//    num = Var(curVal.apply() + x)
//  }
//
//}
//  def consolidated(usage: List[Bandwidth]) = Signal(usage.map(_.num()).sum)
//
//  val a = new Bandwidth()
//  val total = consolidated(List(a))
//
//a Usage 9
//total; 2
//println (a.num())

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.{ ActorRef, ActorSystem, Behavior }

object HelloWorld {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])
  final case class Greeted(whom: String, from: ActorRef[Greet])

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    context.log.info("Hello {}!", message.whom)
    message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }


}