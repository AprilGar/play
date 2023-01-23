package controllers

import baseSpec.BaseSpecWithApplication
import models.{APIError, DataModel}
import org.scalamock.scalatest.MockFactory
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContent, AnyContentAsEmpty, Result}
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
    "abc",
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

  "ApplicationController .read()" should {
    "find a book in the database by id" in {
      val createRequest: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      (mockRepository.create(_: DataModel)).expects(dataModel).returning(Future(Right(dataModel)))
      val createdResult: Future[Result] = TestApplicationController.create()(createRequest)
      status(createdResult) shouldBe Status.CREATED

      val readRequest: FakeRequest[AnyContent] = buildGet("/read/abcd")
      (mockRepository.read(_: String)).expects(*).returning(Future(Right(dataModel)))
      val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest)
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[DataModel] shouldBe dataModel
    }

    "throw an error if id cannot be found" in {
      val readRequest: FakeRequest[AnyContent] = buildGet("/read/abcd")
      (mockRepository.read(_: String)).expects(*).returning(Future(Left(APIError.BadAPIResponse(400, "Books cannot be read"))))
      val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest)
      status(readResult) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }

  "ApplicationController .update()" should {

    "update a book using the id" in {
      val createRequest: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      (mockRepository.create(_: DataModel)).expects(dataModel).returning(Future(Right(dataModel)))
      val createdResult: Future[Result] = TestApplicationController.create()(createRequest)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](Json.toJson(dataModel2))
      (mockRepository.update(_:String, _:DataModel)).expects(*,*).returning(Future(Right(dataModel)))
      val updateResult: Future[Result] = TestApplicationController.update("abcd")(updateRequest)
      status(updateResult) shouldBe Status.ACCEPTED
    }

    "give an bad request error if it cannot update a book due to incorrect type" in {
      val request: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](Json.obj())
      (mockRepository.update(_:String, _:DataModel)).expects(*,*).returning(Future(Left(APIError.BadAPIResponse(400, "Books cannot be updated")))).never()
      val updatedResult = TestApplicationController.update("abcd")(request)
      status(updatedResult) shouldBe Status.BAD_REQUEST
    }
  }

  "ApplicationController .readByName()" should {
    "find a book in the database using the name" in {
      val createRequest: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      (mockRepository.create(_: DataModel)).expects(dataModel).returning(Future(Right(dataModel)))
      val createdResult: Future[Result] = TestApplicationController.create()(createRequest)
      status(createdResult) shouldBe Status.CREATED

      val readNameRequest: FakeRequest[AnyContent] = buildGet("/read/abcd")
      (mockRepository.findByName(_:String)).expects(*).returning(Future(Right(dataModel)))
      val readNameResult: Future[Result] = TestApplicationController.readByName("test name")(readNameRequest)
      status(readNameResult) shouldBe Status.OK
      contentAsJson(readNameResult).as[DataModel] shouldBe dataModel
    }

    "will throw an error if cannot find book using the name" in {
      val readNameRequest: FakeRequest[AnyContent] = buildGet("/read/abc")
      (mockRepository.findByName(_:String)).expects(*).returning(Future(Left(APIError.BadAPIResponse(404, "Book cannot be found"))))
      val readNameResult: Future[Result] = TestApplicationController.readByName("")(readNameRequest)
      status(readNameResult) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }

  "ApplicationController .delete()" should {
    "delete a book using the id" in {
      val createRequest: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      (mockRepository.create(_: DataModel)).expects(dataModel).returning(Future(Right(dataModel)))
      val createdResult: Future[Result] = TestApplicationController.create()(createRequest)
      status(createdResult) shouldBe Status.CREATED

      val deleteRequest: FakeRequest[AnyContentAsEmpty.type] = buildDelete("delete/abcd")
      (mockRepository.delete(_:String)).expects(*).returning(Future(Right("book deleted")))
      val deleteResponse: Future[Result] = TestApplicationController.delete("abcd")(deleteRequest)
      status(deleteResponse) shouldBe Status.ACCEPTED
    }

    "not delete a book if id cannot be found" in {
      val deleteRequest: FakeRequest[AnyContentAsEmpty.type] = buildDelete("delete/abd")
      (mockRepository.delete(_:String)).expects(*).returning(Future(Left(APIError.BadAPIResponse(400, "Books cannot be deleted"))))
      val deleteResponse: Future[Result] = TestApplicationController.delete("abd")(deleteRequest)
      status(deleteResponse) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }
}
