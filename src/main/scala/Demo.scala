import scala.collection.immutable
import scala.collection.parallel.immutable

object Demo {

  def main(args: Array[String]): Unit = {


    //{ case class User(var Bandwidth : Int = 100, var name : String = "", var time : Int = 90) {

    //def findHighestConsumer(user1 : User, user2 : User)

    //{ val theHighestConsumer =
    //if(s1.Bandwidth > s2.Bandwidth)
    //{s1
    // }else {
    // {s2
    // }
    // println(theHighestConsumer.name + "is the highest consumer")
    // }
    // }

    //}
    // val s1 = User(60, "Alice Simi", 89)
    // val s2 = User(80, "Mark James", 70)
    // println(s1)
    //println(s2)

    //s1.Bandwidth > s2.Bandwidth


    //var num = List(4,6,8)
    //for(n <- num) println(n)
    //num. foreach{i:Int => println(i)}
    //}

    case class User(bandwidth: Int = 100, name: String = "", time: Int = 90)

    val s1 = User(120, "Alice Simi", 89)
    val s2 = User(80, "Mark James", 70)

    println(s1, s2)


    def findHighestConsumer(user1: User, user2: User) {
      val theHighestConsumer = if (user1.bandwidth > user2.bandwidth) user1 else user2
      println(theHighestConsumer.name + " is the highest consumer")

    }

    //    val allUsers : User*
    //    val allUsers : Seq[User]

    def denyAll(alluser: User*) {
      alluser.map { u: User =>
        if (u.bandwidth > 100) println(u.name + " is using " + u.bandwidth + "Gbs")
      }
    }

    //val theBandwidthThreshold = if (alluser.bandwidth > 100)
    //          println("deny") else println("allow")


    findHighestConsumer(s1, s2)

    denyAll(s1, s2)

    val highestConsumer: Int = List(s1, s2) map (_.bandwidth) max
    val highestConsumer2: List[Unit] = List(s1, s2).groupBy(_.bandwidth).maxBy(_._1)._2.map { xx =>
      xx.name + " is the highest consumer"

      println(highestConsumer)
//      println(highestConsumer2.mkString)


    }
  }
}