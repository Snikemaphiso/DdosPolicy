package Class

case class User(bandwidth: Int, name: String, time: Int)

 object ManageUsersConnections extends App {

  override def main(args: Array[String]): Unit = {

    val s1 = User(120, "Alice Simi", 55)
    val s2 = User(80, "Mark James", 70)
    val s3 = User(70, "Simi Abraham", 45)
    val s4 = User(101, "James Paul", 89)

    denyAll(s1, s2, s3, s4)

    resetAll(s1, s2, s3, s4)

    findHighestConsumer(s1, s2, s3, s4)

  }

  def denyAll(allUsers: User*): Seq[Unit] = {
    allUsers.map {user =>
      if(user.bandwidth > 100) {
        println(s"${user.name} is using ${user.bandwidth} Bandwidth. Deny connection")
        //TODO: DenyAll users connection
      } else {
        println(s"${user.name} is using ${user.bandwidth} Bandwidth. Allow connection ")
        //TODO: AllowAll users connection
      }
    }
  }
  def resetAll(allUsers: User*): Seq[Unit] = {
    allUsers.map { user =>
      if (user.time > 60) {
        println(s"${user.name} has used ${user.time} minutes. Reset connection")

      } else {
        println(s"${user.name} has used ${user.time} minutes. Allow connection")

      }
    }
  }

  def findHighestConsumer(allUsers: User*) {
    val theHighestConsumer = allUsers
      .groupBy((user: User) => user.bandwidth)
      .maxBy((bandwidthUserTuple: (Int, Seq[User])) => bandwidthUserTuple._1)
      ._2.head
    println(theHighestConsumer.name + " is the highest consumer with " + theHighestConsumer.bandwidth + " bandwidth")
  }


}
