package connectors

import cats.data.EitherT
import models.{APIError, Book, DataModel}
import play.api.libs.json.{JsError, JsSuccess, OFormat}
import play.api.libs.ws.{WSClient, WSResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}



class LibraryConnector @Inject()(ws: WSClient) {
  def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, DataModel]= {
    val request = ws.url(url)
    val response = request.get()
    EitherT {
      response.map {
          result =>
            result.json.validate[Book] match {
              case JsSuccess(returnedBook, _ ) => Right(DataModel(returnedBook.items.head.id,
                returnedBook.items.head.volumeInfo.title,
                returnedBook.items.head.volumeInfo.description,
                returnedBook.items.head.volumeInfo.pageCount))
              case JsError(errors) => Left(APIError.BadAPIResponse(500, "Could not connect"))
            }
        }
    }
  }

}