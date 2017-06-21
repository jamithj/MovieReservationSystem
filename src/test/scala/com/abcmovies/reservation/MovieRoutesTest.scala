package com.abcmovies.reservation

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.abcmovies.reservation.JsonStructure._
import com.abcmovies.reservation.MovieDB._

/**
  * Created by jamit on 12/06/2017.
  */
class MovieRoutesTest extends WordSpec with ScalaFutures with Matchers with ScalatestRouteTest with DatabaseConfig {
  createTables()
  "Movie api" should {
    "add a movie" in {
      val addAMovieResponse = addAMovie(JsonAddMovieRequest("tt0111161", "The Shawshank Redemption"))

      for (
        mrrFuture <- addAMovieResponse;
        mrr = mrrFuture.right.get
      ) yield {
        assert(mrr.imdbId == "tt0111161")
      }
    }

    "register a movie" in {
      val registerMovieResponse = registerAMovie(JsonMovieRegisterRequest("tt0111161", 100, "screen_123456"))

      for (
        mrrFuture <- registerMovieResponse;
        mrr = mrrFuture.right.get
      ) yield {
        assert(mrr.imdbId == "tt0111161")
      }
    }

    "reserve a seat" in {
      val reserveSeatResponse = reserveASeat(JsonReserveSeatRequest("tt0111161", "screen_123456"))

      for (
        mrrFuture <- reserveSeatResponse;
        mrr = mrrFuture.right.get
      ) yield {
        assert(mrr.imdbId == "tt0111161")
      }
    }

    "retrieve a movie" in {
      val retrieveMovieResponse = retrieveMovie(JsonRetrieveMovieRequest("tt0111161", "screen_123456"))

      for (
        mrrFuture <- retrieveMovieResponse;
        mrr = mrrFuture.right.get
      ) yield {
        assert(mrr.imdbId == "tt0111161")
      }
    }

  }

}

