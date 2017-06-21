package com.abcmovies.reservation

/**
  * Created by jamit on 14/06/2017.
  */

import akka.util.Timeout
import com.abcmovies.reservation.JsonStructure._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import spray.json._
import scala.io.StdIn
import scala.concurrent.duration._

object Main extends App with JsonSupport with DatabaseConfig{
  implicit val system = ActorSystem("MovieReservationSystem")
  implicit val materializer = ActorMaterializer()


  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 minutes)

  createTables()

  val route: Route =
    pathPrefix("movie") {
      path("add-a-movie") {
        post {
          entity(as[JsonAddMovieRequest]) { j_movie_add_req =>
            System.out.println("j_movie_reg_req -> " + j_movie_add_req.imdbId.toString)
            complete(MovieDB.addAMovie(j_movie_add_req).map(_.toJson))
          }
        }
      } ~
      path("register-a-movie") {
        post {
          entity(as[JsonMovieRegisterRequest]) { j_movie_reg_req =>
            System.out.println("j_movie_reg_req -> " + j_movie_reg_req.imdbId.toString)
            complete(MovieDB.registerAMovie(j_movie_reg_req).map(_.toJson))
          }
        }
      } ~
        path("reserve-a-seat") {
          post {
            entity(as[JsonReserveSeatRequest]) { j_seat_res_req =>
              System.out.println("j_seat_res_req -> " + j_seat_res_req.imdbId.toString)
              complete{MovieDB.reserveASeat(j_seat_res_req).map(_.toJson)}
            }
          }
        }  ~
        path("retrieve-a-movie") {
          post {
            entity(as[JsonRetrieveMovieRequest]) { j_movie_ret_req =>
              System.out.println("j_movie_ret_req -> " + j_movie_ret_req.imdbId.toString)
              complete(MovieDB.retrieveMovie(j_movie_ret_req).map(res => res.toJson))
            }
          }
        }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 9003)
  println(s"Server online at http://localhost:9003/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}