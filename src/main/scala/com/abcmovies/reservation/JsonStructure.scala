package com.abcmovies.reservation

/**
  * Created by jamit on 12/06/2017.
  */

import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.abcmovies.reservation.MovieDB.{DbMovie, DbMovieReservation}
import spray.json._

object JsonStructure {
  case class JsonAddMovieRequest(imdbId: String, title: String)
  case class JsonMovieRegisterRequest(imdbId: String, availableSeats: Int, screenId: String)
  case class MovieRegisterRequest(imdbId: String, availableSeats: Int, screenId: String)
  case class MovieRegisterResponse(imdbId: String)
  case class JsonMovieRegisterResponse(imdbId: String)

  case class JsonReserveSeatRequest(imdbId: String, screenId: String)
  case class ReserveSeatRequest(imdbId: String, screenId: String)
  case class ReserveSeatResponse(imdbId: String, successful: Boolean, exceptionID: Option[String], exceptionMessage: Option[String])
  case class JsonReserveSeatResponse(imdbId: String, successful: Boolean, exceptionID: Option[String], exceptionMessage: Option[String])

  case class JsonRetrieveMovieRequest(imdbId: String, screenId: String)
  case class RetrieveMovieRequest(imdbId: String, screenId: String)
  case class RetrieveMovieResponse(imdbId: String, title: String, screenId: String, availableSeats: Int, reservedSeats: Int)
  case class JsonRetrieveMovieResponse(imdbId: String, title: String, screenId: String, availableSeats: Int, reservedSeats: Int)
  case class JsonTransactionStatus(successful: Boolean, ErrorMessage: Option[JsonException])

  case class JsonException(id: String, message: String)
  case class Movie(imdbId: String, title: String, screenId: String, availableSeats: Int, reservedSeats: Int)
  case class MovieToRegister(imdbId: String, screenId: String, availableSeats: Int, reservedSeats: Int)

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val JsonMovieRegisterRequestFormat = jsonFormat3(JsonMovieRegisterRequest)
    implicit val JsonMovieRegisterResponseFormat = jsonFormat1(JsonMovieRegisterResponse)
    implicit val JsonReserveSeatRequestFormat = jsonFormat2(JsonReserveSeatRequest)
    implicit val JsonReserveSeatResponseFormat = jsonFormat4(JsonReserveSeatResponse)
    implicit val JsonRetrieveMovieRequestFormat = jsonFormat2(JsonRetrieveMovieRequest)
    implicit val JsonRetrieveMovieResponseFormat = jsonFormat5(JsonRetrieveMovieResponse)
    implicit val JsonDbMovieReservationFormat = jsonFormat5(DbMovieReservation)
    implicit val JsonErrFormat = jsonFormat1(Err)
    implicit val JsonAddMovieRequestFormat = jsonFormat2(JsonAddMovieRequest)
    implicit val JsonMovieFormat = jsonFormat3(DbMovie)
  }

  def fromJsonAddMovieRequest(in: JsonAddMovieRequest) = {
    MovieDB.DbMovie(scala.util.Random.nextLong(), in.imdbId, in.title)
  }

  def fromJsonMovieRegisterRequest(in: JsonMovieRegisterRequest) = {
    MovieDB.DbMovieReservation(scala.util.Random.nextLong(), in.imdbId, in.screenId, in.availableSeats, 0)
  }

  def toJsonMovieRegisterResponse(in: MovieRegisterResponse) =
    JsonMovieRegisterResponse(in.imdbId)

  def fromJsonReserveSeatRequest(in: JsonReserveSeatRequest) =
    ReserveSeatRequest(in.imdbId, in.screenId)

  def fromJsonRetrieveMovieRequest(in: JsonRetrieveMovieRequest) =
    RetrieveMovieRequest(in.imdbId, in.screenId)

  case class ErrorString(error: String)
  object ErrorString { implicit val f = jsonFormat1(ErrorString.apply) }

}
