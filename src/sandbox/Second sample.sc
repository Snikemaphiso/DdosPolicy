

println("Hello World")

var num1 : Int = 8
var num2 : Int = 4

num1 + num2


case class User(bandwidth: Int = 100, name: String = "", time: Int = 100)

val s1 = User(82, "Mark John", 50)
val s2 = User(70, "Michael Schofield", 90)

def findHighestConsumer2(users: User*): Unit = {
  val theHighestConsumer: User = users.maxBy(_.bandwidth)
  println(theHighestConsumer.name + " is the highest consumer 2")
}

findHighestConsumer2(s1, s2)

val highestConsumer: Int = List(s1, s2).map(_.bandwidth).max
val highestConsumer2: List[String] = List(s1, s2).groupBy(_.bandwidth).maxBy(_._1)._2.map { xx => xx.name + " is the highest consumer" }
  println(highestConsumer)
  println(highestConsumer2)



//    val netOb = Net.findHighestConsumer(s1, s2)
