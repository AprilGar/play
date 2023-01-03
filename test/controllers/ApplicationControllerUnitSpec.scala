package controllers

import baseSpec.BaseSpecWithApplication
import models.{APIError, DataModel}
import org.scalamock.scalatest.MockFactory
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContent, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}
import repositories.repositories.DataRepoTrait

import scala.concurrent.Future

class ApplicationControllerUnitSpec extends BaseSpecWithApplication with MockFactory{

  val mockRepository: DataRepoTrait = mock[DataRepoTrait]

  val TestApplicationController = new ApplicationController(component, mockRepository, executionContext, service)

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100
  )

  private val dataModel2: DataModel = DataModel(
    "abcd",
    "test name2",
    "test description",
    100
  )

  private val seqOfDataModel: Seq[DataModel] = Seq(dataModel)

  "ApplicationController .index()" should {
    "return a sequence of books" in {
      val indexRequest: FakeRequest[AnyContent] = buildGet("/api")
      (() => mockRepository.index()).expects().returning(Future(Right(seqOfDataModel)))
      val result = TestApplicationController.index()(indexRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).as[Seq[DataModel]] shouldBe seqOfDataModel
    }

    "return a error" in {
      val indexRequest: FakeRequest[AnyContent] = buildGet("/api")
      (() => mockRepository.index()).expects().returning(Future(Left(APIError.BadAPIResponse(404, "Books cannot be found"))))
      val result = TestApplicationController.index()(indexRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      contentAsJson(result) shouldBe Json.toJson("Bad response from upstream; got status: 404, and got reason Books cannot be found")
    }
  }

  "ApplicationController .create()" should {
    "create a book in the database" in {
      val createRequest: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      (mockRepository.create(_:DataModel)).expects(dataModel).returning(Future(Right(dataModel)))
      val createdResult: Future[Result] = TestApplicationController.create()(createRequest)
      status(createdResult) shouldBe Status.CREATED
    }

    "give an bad request error if it cannot create a book in the database" in {
      val createRequest: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.obj())
      (mockRepository.create(_: DataModel)).expects(*).returning(Future(Left(APIError.BadAPIResponse(424, "Books cannot be created")))).never()
      val createdResult = TestApplicationController.create()(createRequest)
      status(createdResult) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }

}
