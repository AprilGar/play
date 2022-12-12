package models

import play.api.libs.json.{Json, OFormat}

case class Items(id: String, volumeInfo: VolumeInfo, pageCount: Int)

case class VolumeInfo(title: String, description: String)

case class Book(items: List[Items])

object Items {
  implicit val formats: OFormat[Items] = Json.format[Items]
}

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}
