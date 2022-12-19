package repositories

package repositories

import models.{APIError, Book, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model.{Filters, _}
import org.mongodb.scala.result
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DataRepository @Inject()(
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] = {
    collection.find().toFuture().map {
      case books: Seq[DataModel] => Right(books)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }
  }

  def create(book: DataModel): Future[Either[APIError, DataModel]] =
    collection.insertOne(book).toFutureOption()
      .map{
      case Some(result) if result.wasAcknowledged() => Right(book)
      case _ => Left(APIError.BadAPIResponse(424, "Books cannot be created"))
    }

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def read(id: String): Future[Either[APIError, DataModel]] =
    collection.find(byID(id)).headOption flatMap {
      case Some(data) => Future(Right(data))
      case _ => Future(Left(APIError.BadAPIResponse(400, "Books cannot be read")))
    }

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection.replaceOne (
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true)
    ).toFutureOption() map{
      case Some(result) if result.wasAcknowledged() => Right(book)
      case _ => Left(APIError.BadAPIResponse(401, "Books cannot be updated"))
    }

  def delete(id: String): Future[Either[APIError, String]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFutureOption().map{
      case Some(result) if result.wasAcknowledged() => Right("book deleted")
      case _ => Left(APIError.BadAPIResponse(400, "Books cannot be deleted"))
    }

  def deleteAll(): Future[Unit] =
    collection.deleteMany(empty()).toFuture().map(_ => ())

  def filterByName(name: String): Bson = {
    Filters.and(
      Filters.equal("name", name))
  }

  def findByName(name: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]] = {
    collection.find(filterByName(name)).headOption flatMap {
      case (correctName) => Future(Right(correctName))
      case (_) => Future(Left(APIError.BadAPIResponse(404, "Book cannot be found")))
    }
  }

}
