package com.abcmovies.reservation

/**
  * Created by jamit on 12/06/2017.
  */

import com.abcmovies.reservation.JsonStructure._
import com.abcmovies.reservation.MovieDB._

  case class Movie(imdbId: String, title: String, screenId: String, availableSeats: Int, reservedSeats: Int) {

    def register(in: JsonMovieRegisterRequest): Either[Exception, Option[String]] = {
       Right(testMovieRservation.find(_.imdbId == in.imdbId).map(_.imdbId))
     }

    def reserveASeat(in: JsonReserveSeatRequest): Either[Exception,Option[Movie]] = {
      Right(testMovieRservation.find(_.imdbId == in.imdbId).map(mv => Movie(mv.imdbId, "", mv.screenId, mv.availableSeats - 1, mv.reservedSeats + 1)))
    }

    def retrieveMovie(in: JsonRetrieveMovieRequest): Either[Exception,Option[Movie]] = {
      Right(
      for (
        mr <- testMovieRservation.find(_.imdbId == in.imdbId);
        movie <- testMovies.find(_.imdbId == mr.imdbId)
      ) yield {
        Movie(mr.imdbId, movie.title, mr.screenId, mr.availableSeats, mr.reservedSeats)
      })
    }
  }

