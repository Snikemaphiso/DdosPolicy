package controllers

import com.github.blemale.scaffeine.{AsyncCache, Scaffeine}
import models.DdosPlayJsonImplicits.policyWrites
import models.Policy
import play.api.libs.json.Json

import scala.collection.mutable
import scala.concurrent.duration.DurationInt

trait Cache {

  /**
   * key is a string and we're using the name of the policy as key since no two policies should have the same name.
   * The value is the actual policy we are saving.
   */
  protected val policyCache: AsyncCache[String, Policy] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour) //make this configurable
      .maximumSize(500) //make this configurable
      .buildAsync()


  def policies: mutable.Map[String, Policy] = policyCache.synchronous().asMap()

  def getAllCachedPoliciesKeySet: Seq[String] = policies.keys.toList

  def getAllCachedPolicyValues: Seq[Policy] = policies.values.toList

  def printAllPoliciesInCache: String = {
    getAllCachedPolicyValues.map(p => Json.prettyPrint(Json.toJson[Policy](p))).mkString("\n")
  }

}
