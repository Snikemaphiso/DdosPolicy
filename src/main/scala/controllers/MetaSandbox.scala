package controllers

import scala.meta._

object MetaSandbox extends App {

  val program = """object Main extends App { print("Hello!") }"""
  val tree = program.parse[Source].get

  println(tree.syntax)
}
