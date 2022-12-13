package models

import play.api.libs.json.{Json, OFormat}

case class Item(id: String, volumeInfo: VolumeInfo, pageCount: Int)

case class VolumeInfo(title: String, description: String)

case class Book(items: List[Item])

object Item {
  implicit val formats: OFormat[Item] = Json.format[Item]
}

object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}
