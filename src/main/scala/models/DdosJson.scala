package models

trait DdosJson extends DdosInputType {
  def getDdosInputFromJson(jsonString: String): Seq[DdosInputType]
}
