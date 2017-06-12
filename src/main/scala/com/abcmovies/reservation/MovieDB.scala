package com.abcmovies.reservation

/**
  * Created by jamit on 12/06/2017.
  */
object MovieDB {
  case class MovieDao(imdbId: String, title: String)
  case class MovieReservationDao(imdbId: String, screenId: String, availableSeats: Int, reservedSeats: Int)
   val testMovies = List(MovieDao("tt0111161", "The Shawshank Redemption"), MovieDao("tt0111162", "The Star Wars"))
   val testMovieRservation = List(MovieReservationDao("tt0111161", "screen_123456", 100, 20), MovieReservationDao("tt0111162", "screen_78910", 75, 25))
}
