name := "MovieReservationSystem"

version := "0.0.1"

scalaVersion := "2.11.8"

//assemblyJarName in assembly := "mrs.jar"

//target in assembly := baseDirectory.value

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
    "io.spray" % "spray-json_2.11" % "1.3.2"
  )
}