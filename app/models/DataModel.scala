package models

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String, name: String, description: String, numSales: Int)

object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
//  this allow for transformation to and from JSON
}