package models

trait DdosJson {
  def getDdosInputFromJson(jsonString: String): Seq[DdosInputType]
}
