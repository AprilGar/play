package controllers

import akka.protobufv3.internal.Service
import connectors.LibraryConnector
import models.{APIError, DataModel}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import play.filters.csrf.CSRF
import repositories.repositories.{DataRepoTrait, DataRepository}
import service.LibraryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepoTrait, implicit val ec: ExecutionContext, val service: LibraryService)
  extends BaseController with play.api.i18n.I18nSupport {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map{
      case Right(item: Seq[DataModel]) => Ok {Json.toJson(item)}
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }


  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(InternalServerError)
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.read(id).map{
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def readByName(name: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.findByName(name).map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
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
    dataRepository.delete(id).map{
      case Right(deletedBook: String) => Accepted
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    service.getGoogleBook(search = search, term = term).value.map {
      case Right(book) => Ok(Json.toJson(book))
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def findBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    service.getGoogleBook(search = search, term = term).value.map {
      case Right(book) => Ok(views.html.findBook(book))
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def findBookFromDB(name: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.findByName(name = name).map {
      case Right(book: DataModel) => Ok(views.html.findBook(book))
      case Left(error) => Status(error.httpResponseStatus)(Json.toJson(error.reason))
    }
  }

  def accessToken(implicit request: Request[_]) = {
    CSRF.getToken
  }

  def addBook: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.addBook(DataModel.bookForm))
  }

  def addBookForm(): Action[AnyContent] = Action.async { implicit request =>
    accessToken
    DataModel.bookForm.bindFromRequest().fold(
      formWithErrors => {
        Future(BadRequest(formWithErrors.toString))
      },
      formData => {
        dataRepository.create(formData).map{
          case Right(book) => Redirect(routes.ApplicationController.findBookFromDB(book.name))
          case  Left(error) => (BadRequest(error.toString))
        }
      }
    )
  }

}
