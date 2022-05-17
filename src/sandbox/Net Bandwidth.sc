
//case class User(var Bandwidth: Int = 100, var name: String = "", var time: Int = 90) {

//  object bandwidth



//def findHighestConsumer(user1 : User, user2 : User) {
//  val theHighestConsumer =
//    if (user1.Bandwidth > user2.Bandwidth) user1 else user2
//
//
//}
//  val s1 = User(60, "Alice Simi", 89)
//  val s2 = User(80, "Mark James", 70)
//  findHighestConsumer(s1, s2)
//
//}


//// an event type
//trait Event
//
//// an event handler
//trait EventHandler[-T <: Event] {
//  def handle(event: T)
//}
//
//// a map to register the event handlers
//val eventRegistry =
//  new Map[Class[_], EventHandler[_]]()