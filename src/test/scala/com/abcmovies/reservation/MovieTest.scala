package com.abcmovies.reservation

import com.abcmovies.reservation.JsonStructure.{JsonMovieRegisterRequest, JsonReserveSeatRequest, JsonRetrieveMovieRequest}
import com.abcmovies.reservation.MovieDB._
import com.abcmovies.reservation.Movie._
import org.scalatest.FunSuite

/**
  * Created by jamit on 12/06/2017.
  */
class MovieTest extends FunSuite {

  val movie = testMovies.head
  val movieReservation = testMovieRservation.head
  val testMovie = Movie(movie.imdbId, movie.title, movieReservation.screenId, movieReservation.availableSeats, movieReservation.reservedSeats)

  test("should be able to register a movie") {
    val registrationRequest = JsonMovieRegisterRequest("tt0111161", 80, "screen_123456")
    assert(testMovie.register(registrationRequest) == Right(Some(testMovie.imdbId)))
  }

  test("should be able to reserve a seat") {
    val reserveASeatRequest = JsonReserveSeatRequest("tt0111161", "screen_123456")
    val reservedMovieResponse = testMovie.reserveASeat(reserveASeatRequest)
    assert(reservedMovieResponse.isRight)
    assert(reservedMovieResponse.right.get.nonEmpty)
    val reserverdASeatMovie = reservedMovieResponse.right.get.get
    assert(reserverdASeatMovie.reservedSeats == testMovie.reservedSeats + 1)
    assert(reserverdASeatMovie.availableSeats == testMovie.availableSeats - 1)
  }

  test("should be able to retrieve a movie") {
    val reserveASeatRequest = JsonRetrieveMovieRequest("tt0111161", "screen_123456")
    val retriveMovieResponse = testMovie.retrieveMovie(reserveASeatRequest)
    assert(retriveMovieResponse.isRight)
    assert(retriveMovieResponse.right.get.nonEmpty)
    val movie = retriveMovieResponse.right.get.get
    assert(movie.imdbId == reserveASeatRequest.imdbId)
    assert(movie.screenId == reserveASeatRequest.screenId)
  }

}
