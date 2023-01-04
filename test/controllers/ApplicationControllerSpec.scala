package controllers

import akka.util.ByteString
import baseSpec.BaseSpecWithApplication
import models.DataModel
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.Accumulator
import play.api.mvc.{AnyContent, AnyContentAsEmpty, Result}
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(component, repository, executionContext, service)

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

  private val googleBookResponse: DataModel = DataModel(
    "n3OwneMiBPgC",
    "The Umbrella Conspiracy",
    "When a remote mountain community is suddenly beset by a rash of grisly murders, the Special Tactics and Rescue Squad--a paramilitary unit--is dispatched to investigate",
    293
  )

  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()

  "ApplicationController .index()" should {
    "return a sequence of books" in {
      beforeEach()
      val result = TestApplicationController.index()(FakeRequest())
      status(result) shouldBe Status.OK
      afterEach()
    }
  }

  "ApplicationController .create()" should {
    "create a book in the database" in {
      beforeEach()
        val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
        val createdResult: Future[Result] = TestApplicationController.create()(request)
        status(createdResult) shouldBe Status.CREATED
      afterEach()
    }

    "give an bad request error if it cannot create a book in the database" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.obj())
      val createdResult = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.INTERNAL_SERVER_ERROR
      afterEach()
    }
  }

  "ApplicationController .read()" should {
    "find a book in the database by id" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val readRequest: FakeRequest[AnyContent] = buildGet("/read/abcd")
      val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest)
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[DataModel] shouldBe dataModel
      afterEach()
    }

    "throw an error if id cannot be found" in {
      beforeEach()
      val readRequest: FakeRequest[AnyContent] = buildGet("/read/abcd")
      val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest)
      status(readResult) shouldBe Status.INTERNAL_SERVER_ERROR
      afterEach()
    }
  }

  "ApplicationController .readByName()" should {
    "find a book in the database using the name" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val readNameRequest: FakeRequest[AnyContent] = buildGet("/read/abcd")
      val readNameResult: Future[Result] = TestApplicationController.readByName("test name")(readNameRequest)
      status(readNameResult) shouldBe Status.OK
      contentAsJson(readNameResult).as[DataModel] shouldBe dataModel
      afterEach()
    }

    "will throw an error if cannot find book using the name" in {
      beforeEach()
      val readNameRequest: FakeRequest[AnyContent] = buildGet("/read/abc")
      val readNameResult: Future[Result] = TestApplicationController.readByName("")(readNameRequest)
      status(readNameResult) shouldBe Status.INTERNAL_SERVER_ERROR
      afterEach()
    }
  }

  "ApplicationController .update()" should {

    "update a book using the id" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildPut("/update/abcd").withBody[JsValue](Json.toJson(dataModel2))
      val updateResult: Future[Result] = TestApplicationController.update("abcd")(updateRequest)
      status(updateResult) shouldBe Status.ACCEPTED
      afterEach()
    }

    "give an bad request error if it cannot update a book" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.obj())
      val createdResult = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.INTERNAL_SERVER_ERROR
      afterEach()
    }

  }

  "ApplicationController .delete()" should {
    "delete a book using the id" in {
      beforeEach()

      val request: FakeRequest[JsValue] = buildPost("/create").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val deleteRequest: FakeRequest[AnyContentAsEmpty.type] = buildDelete("delete/abcd")
      val deleteResponse: Future[Result] = TestApplicationController.delete("abcd")(deleteRequest)
      status(deleteResponse) shouldBe Status.ACCEPTED

      afterEach()
    }

    "not delete a book if id cannot be found" in {
      val deleteRequest: FakeRequest[AnyContentAsEmpty.type] = buildDelete("delete/abc")
      val deleteResponse: Future[Result] = TestApplicationController.delete("abc")(deleteRequest)
      status(deleteResponse) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }

  "ApplicationController .getGoogleBook()" should {
    "get a book using search and term" in {
      beforeEach()

      val requestToGoogle = buildGet("/library/google/n3OwneMiBPgC/umbrella")
      val resultFromGoogle = TestApplicationController.getGoogleBook("n3OwneMiBPgC", "umbrella")(requestToGoogle)

      status(resultFromGoogle) shouldBe Status.OK
      contentAsJson(resultFromGoogle) shouldBe Json.toJson(googleBookResponse)

      afterEach()
    }

    "should not get a book using search and term if they don't match url" in {
      beforeEach()
      val requestToGoogle = buildGet("/library/google/n3OwneMiBPgC/umbrella")
      val resultFromGoogle = TestApplicationController.getGoogleBook("n3OwneM", "monkey")(requestToGoogle)
      status(resultFromGoogle) shouldBe Status.INTERNAL_SERVER_ERROR
      afterEach()
    }
  }

}
