package com.abcmovies.reservation

/**
  * Created by jamit on 12/06/2017.
  */
object JsonStructure {
  case class JsonMovieRegisterRequest(imdbId: String, availableSeats: Int, screenId: String)
  case class JsonMovieRegisterResponse(imdbId: String, tranactionStatus: JsonTransactionStatus)
  case class JsonReserveSeatRequest(imdbId: String, screenId: String)
  case class JsonReserveSeatResponse(imdbId: String, tranactionStatus: JsonTransactionStatus)
  case class JsonRetrieveMovieRequest(imdbId: String, screenId: String)
  case class JsonRetrieveMovieResponse(movie: Movie, tranactionStatus: Option[JsonTransactionStatus])
  case class JsonTransactionStatus(successful: Boolean, ErrorMessage: Option[JsonException])
  case class JsonException(id: String, message: String)

}
