package service

import baseSpec.BaseSpec
import cats.data.EitherT
import connectors.LibraryConnector
import jdk.net.SocketFlow.Status
import models.{APIError, Book, DataModel}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsObject, JsValue, Json, OFormat}

import scala.concurrent.{ExecutionContext, Future}

class LibraryServiceSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite {

  val mockConnector = mock[LibraryConnector]
  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testService = new LibraryService(mockConnector)
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]

  val gameOfThrones: JsValue = Json.obj(
    "_id" -> "someId",
    "name" -> "A Game of Thrones",
    "description" -> "The best book!!!",
    "numSales" -> 100
  )

  "getGoogleBook" should {
    val url: String = "testUrl"

        "return a book" in {
          (mockConnector.get[DataModel](_: String)(_: OFormat[DataModel], _: ExecutionContext))
            .expects(url, *, *).returning(EitherT.rightT[Future, APIError](gameOfThrones.as[DataModel])).once()

          whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
            result shouldBe Right(DataModel("someId", "A Game of Thrones", "The best book!!!", 100))
          }
        }

    "return an APIError" in {
      (mockConnector.get[JsObject](_: String)(_: OFormat[JsObject], _: ExecutionContext))
              .expects(url, *, *).returning(EitherT.leftT[Future, DataModel](APIError.BadAPIResponse(500, "Could not connect"))).once()

        whenReady(testService.getGoogleBook(urlOverride = Some(url), search = "", term = "").value) { result =>
              result shouldBe  Left(APIError.BadAPIResponse(500, "Could not connect"))
        }
    }
  }
}
