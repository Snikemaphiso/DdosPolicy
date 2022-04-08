package sandbox

  object Net {

    case class User(var Bandwidth: Int = 100, var name: String = "", var time: Int = 90)

      def findHighestConsumer(user1: User, user2: User): Unit = {
        val theHighestConsumer =
          if (user1.Bandwidth > user2.Bandwidth) {
            user1
          } else {
            user2
          }
        val s1 = User(60, "Alice Simi", 89)
        val s2 = User(80, "Mark James", 70)
        println(s1, s2)
        println(theHighestConsumer.name + "is the highest consumer")
      }

  }

