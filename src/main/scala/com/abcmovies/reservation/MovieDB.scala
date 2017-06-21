package com.abcmovies.reservation

/**
  * Created by jamit on 12/06/2017.
  */

import java.util.logging.Logger

import com.abcmovies.reservation.JsonStructure._
import slick.driver.H2Driver.api._
import slick.dbio.NoStream
import slick.lifted.TableQuery
import slick.profile.{FixedSqlStreamingAction, SqlAction}

import scala.concurrent.Future
import MovieDB._
import akka.actor.Status.Success
import com.abcmovies.reservation.core.EitherErr
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Try}


object MovieDB extends MovieDaoHelper {

  case class DbMovie(num: Long, imdbId: String, title: String)

  class DbMovies(tag: Tag) extends Table[DbMovie](tag, "Movie") {
    def movieNum = column[Long]("movie_num", O.PrimaryKey)

    def imdbId = column[String]("imdbId")

    def title = column[String]("title")

    def * = (movieNum, imdbId, title) <> (DbMovie.tupled, DbMovie.unapply)
  }

  val db_movies = TableQuery[DbMovies]

  case class DbMovieReservation(num: Long, imdbId: String, screenId: String, availableSeats: Int, reservedSeats: Int)

  class DbMovieReservations(tag: Tag) extends Table[DbMovieReservation](tag, "MovieReservation") {
    def reservationNum = column[Long]("reservation_num", O.PrimaryKey)

    def imdbId = column[String]("imdbId")

    def screenId = column[String]("screen_id")

    def availableSeats = column[Int]("available_seats")

    def reservedSeats = column[Int]("reserved_seats")

    def * = (reservationNum, imdbId, screenId, availableSeats, reservedSeats) <> (DbMovieReservation.tupled, DbMovieReservation.unapply)
  }

  val db_movie_reservations = TableQuery[DbMovieReservations]

  def addAMovie(movie: JsonAddMovieRequest): Future[EitherErr[DbMovie]] = {
    val movieReservRecord = fromJsonAddMovieRequest(movie)
    runQuery(db, db_movies += fromJsonAddMovieRequest(movie)).map { res =>
      res match {
        case Left(err) => Left(err)
        case Right(res) => Right(movieReservRecord)
      }
    }
  }


  def registerAMovie(movie: JsonMovieRegisterRequest): Future[EitherErr[DbMovieReservation]] = {
    val movieReservRecord = fromJsonMovieRegisterRequest(movie)
    runQuery(db, movieReservationTable += fromJsonMovieRegisterRequest(movie)).map { res =>
      res match {
        case Left(err) => Left(err)
        case Right(res) => Right(movieReservRecord)
      }
    }
  }

  def reserveASeat(in: JsonReserveSeatRequest): Future[EitherErr[DbMovieReservation]] = {
    val request = fromJsonReserveSeatRequest(in)
    var availableSeats = 0
    var reservedSeats = 0
    val movieReservationRecord = movieReservationTable.filter(mrr => mrr.imdbId === request.imdbId && mrr.screenId === request.screenId)
    val result = for (
      mrrFuture <- retrieveMovie(JsonRetrieveMovieRequest(request.imdbId, request.screenId))
      ) yield {
      if (mrrFuture.isRight) {
        val mrr = mrrFuture.right.get
        availableSeats = mrr.availableSeats
        reservedSeats = mrr.reservedSeats
        if (mrr.availableSeats == 0) {
          Future(Left(Err(s"All seats are reserved for the movie ${request.imdbId} ${request.screenId}")))
        } else {
          runQuery(db, movieReservationRecord.map(mrr => (mrr.reservedSeats, mrr.availableSeats)).update((mrr.reservedSeats + 1, mrr.availableSeats - 1)))
        }
      } else {
        Future(Left(Err(s"No movie found to reserve a seat ${request.imdbId} ${request.screenId}")))
      }
    }

    for {
      r1 <- result
      r2 <- r1
    } yield {
      r2 match {
        case Left(err) => Left(err)
        case Right(record) => Right(DbMovieReservation(-1, request.imdbId, request.screenId, availableSeats, reservedSeats))
      }
    }
  }


  def retrieveMovie(in: JsonRetrieveMovieRequest): Future[EitherErr[DbMovieReservation]] = {
    val request = fromJsonRetrieveMovieRequest(in)
    runQuery(db,movieReservationTable.filter(mrr => mrr.imdbId === request.imdbId && mrr.screenId === request.screenId).result.head).map { res =>
      res match {
        case Left(err) => Left(err)
        case Right(res) => Right(res)
      }
    }
  }

}

trait MovieDaoHelper extends DatabaseConfig {
  val movieTable = TableQuery[DbMovies]
  val movieReservationTable = TableQuery[DbMovieReservations]

  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }

  protected implicit def executeReadStreamFromDb[A](action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]): Future[Seq[A]] = {
    db.run(action)
  }

  def errRecover[T]: PartialFunction[Throwable, EitherErr[T]] = {
    case ex: Throwable => Left(dbErr(ex.getMessage))
  }

  def dbErr(ex: Throwable): Err = dbErr(ex.getMessage)

  def dbErr(reason: String): Err = Err(reason)

  def runQuery[T](db: JdbcProfile#Backend#DatabaseDef, query: slick.dbio.DBIO[T]): Future[EitherErr[T]] =
    db.run(query).map(Right(_)) recover {
      errRecover[T]
    }
}


