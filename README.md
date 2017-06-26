Movie Reservation System
========================
Json based API, which facilitate to perform following operations.

1. Add a movie
2. Register a movie
3. Reserve a seat
4. Retrieve movie information.

Persistent store is h2 in memory database. The lifetime is until the jvm is alive.

Request and Response Structures
===============================
1. Add a movie
--------------
Request:
    { "imdbId": "tt0111161",
      "title": "The Shawshank Redemption"
    }

Response
    {
        "imdbId": "tt0111161",
        "title": "The Shawshank Redemption"
    }

2. Register a movie
-------------------
Request:
    {
      "imdbId": "tt0111161",
      "availableSeats": 100,
      "screenId": "screen_123456"
    }

Response:
    {
        "reservedSeats": 0,
        "screenId": "screen_123456",
        "imdbId": "tt0111161",
        "availableSeats": 100
    }

3. Reserve a seat
-----------------
Request:
    {
      "imdbId": "tt0111161",
      "screenId": "screen_123456"
    }

Response:
    {
        "reservedSeats": 0,
        "screenId": "screen_123456",
        "imdbId": "tt0111161",
        "availableSeats": 100
    }

4. Retrieve movie information
-----------------------------
    {
        "imdbId": "tt0111161",
        "screenId": "screen_123456"
    }

Response:
    {
        "reservedSeats": 1,
        "screenId": "screen_123456",
        "imdbId": "tt0111161",
        "availableSeats": 99
    }

Technology Stack
================
Scala 2.11.8
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) Client VM (build 25.131-b11, mixed mode, sharing)
akka-http 10.0.7
slick 3.1.1
h2 db 1.4.187

Run Unit Test
=============
test in sbt console.

Application Testing
===================
Use any rest api client to test the application. Use the json request structures outlined above to test each operation.

Assumption
==========
Movie id imdbId is an external identifier. This should be provided in the request.

Further Enhancements
====================
imdbId should be able to obtain from an external service.
