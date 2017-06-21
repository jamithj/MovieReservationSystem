name := "MovieReservationSystem"

version := "0.0.1"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-unchecked")

resolvers ++= Seq(
  "Artima Maven Repository" at "http://repo.artima.com/releases"
)

libraryDependencies ++= {
  Seq(
    "org.specs2"        %% "specs2"             % "2.3.12"             % "test",
    "org.apache.httpcomponents"        % "httpclient"             % "4.0.2",
    "org.apache.httpcomponents"        % "httpmime"             % "4.3.6",
    "com.mashape.unirest"        % "unirest-java"             % "1.4.9",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    "org.postgresql" % "postgresql" % "9.4.1208",
    "com.h2database" % "h2" % "1.4.187",
    "com.typesafe.slick" %% "slick" % "3.1.1",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.akka" %% "akka-http" % "10.0.7",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.0.7" % Test,
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.7",
    "com.zaxxer" % "HikariCP" % "2.4.7",
    "org.flywaydb" %  "flyway-core" % "3.2.1"
  )
}