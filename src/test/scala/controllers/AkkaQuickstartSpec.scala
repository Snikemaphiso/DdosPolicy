package controllers

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import controllers.PolicyModel.{Alert, Listener}
import org.scalatest.wordspec.AnyWordSpecLike

class AkkaQuickstartSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "A PolicyModel" must {
    "reply to Listener" in {
      val replyProbe = createTestProbe[Listener]()
      val underTest = spawn(PolicyModel())
      underTest ! Alert("Santa", replyProbe.ref)
      replyProbe.expectMessage(Listener("Santa", underTest.ref))
    }
  }

}
