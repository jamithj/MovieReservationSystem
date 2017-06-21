package com.abcmovies.reservation

/**
  * Created by jamit on 13/06/2017.
  */

import com.typesafe.config.ConfigFactory
import slick.driver.H2Driver.api._

trait DatabaseConfig extends Config{

  implicit val session: Session = db.createSession()
  def createTables() {
    val movieTableStmt = session.prepareInsertStatement("CREATE TABLE Movie(movie_num LONG PRIMARY KEY, imdbId VARCHAR NOT NULL, title VARCHAR NOT NULL);")
    movieTableStmt.execute()
    movieTableStmt.close()
    val movieResevationTableStmt = session.prepareInsertStatement("CREATE TABLE MovieReservation(reservation_num LONG PRIMARY KEY, imdbId VARCHAR NOT NULL, screen_id VARCHAR NOT NULL, available_seats INTEGER NOT NULL, reserved_seats INTEGER NOT NULL);")
    movieResevationTableStmt.execute()
    movieResevationTableStmt.close()
  }
}

trait Config {
  private val config = ConfigFactory.load()
  val databaseConfig = config.getConfig("h2mem1")
  val dbUrl = databaseConfig.getString("url")
  val db = Database.forConfig("h2mem1")
}

case class Err(message: String)

package object core {
  type EitherErr[T] = Either[Err, T]
}

