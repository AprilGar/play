package controllers

import akka.protobufv3.internal.Service
import connectors.LibraryConnector
import models.{APIError, DataModel}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import repositories.repositories.DataRepository
import service.LibraryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository, implicit val ec: ExecutionContext, val service: LibraryService) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val books: Future[Seq[DataModel]] = dataRepository.collection.find().toFuture()
    books.map(items => Json.toJson(items)).map(result => Ok(result))
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.read(id)
      .map(book => Ok(Json.toJson(book)))
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
      request.body.validate[DataModel]
      match {
        case JsSuccess(dataModel, _) =>
          dataRepository.update(id, dataModel).map(updatedBook => Accepted)
        case JsError(_) => Future(BadRequest)
      }
}

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id)
      .map(_ => Accepted)
  }

//  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
//    service.getGoogleBook(search = search, term = term).map {
//      book => Ok(Json.toJson(book))
//    }
//  }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    service.getGoogleBook(search = search, term = term).value.map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

}
