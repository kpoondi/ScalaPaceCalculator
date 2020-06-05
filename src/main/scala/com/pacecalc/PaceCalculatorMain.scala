package com.pacecalc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.pacecalc.PaceCalculatorMain.{Pace, Response}
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn


class PaceCalculatorMain {

  def run() :Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val executionContext = system.dispatcher


    implicit val paceFormat = jsonFormat3(Pace)
    implicit val responseFormat = jsonFormat3(Response)

    def resolvePace(paceDouble :Double) :Int = {
      paceDouble match {
        case x if x < 60 => 0
        case _ => 1 + resolvePace(paceDouble - 60)
      }
    }

    def calcPace(hours: Int, minutes :Int, seconds :Int, distance :Double, id: String) :Response = {
      val totalMins = hours * 60 + minutes + (seconds / 60)

      val doublePace = totalMins / distance

      val paceHour = resolvePace(doublePace)
      val paceMin = (doublePace - (paceHour * 60)).toInt
      val paceSec = (((doublePace - (paceHour * 60)) - (paceMin)) * 60).toInt

      Response(Pace(paceHour, paceMin, paceSec), distance, id)
    }

    val calcPaceRoute =
      path("pace") {
        concat(
          post {
            parameter("hour".as[Int], "min".as[Int], "sec".as[Int], "distance".as[Double], "id".as[String]) { (hour, min, sec, miles, id) =>
              complete(calcPace(hour, min, sec, miles, id))
            }
          },
          get {
            parameter("id".as[String]) { id =>
              complete(???)
            }
          }
        )
      }

    val bindingFuture = Http().bindAndHandle(calcPaceRoute , "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

object PaceCalculatorMain extends PaceCalculatorMain with App {
  case class Pace(hour :Int, minute :Int, seconds: Int)
  final case class Response(pace: Pace, distance: Double, id: String)

  run()
}
