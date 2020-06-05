package com.pacecalc

import com.pacecalc.PaceCalculatorMain.Pace
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class PaceStore(tag: Tag) extends Table[(String, Int, Int, Int, Double)] (tag, "PACE_STORE") {
  def id = column[String]("ID", O.PrimaryKey)
  def hour = column[Int]("HOURS")
  def min = column[Int]("MINS")
  def secs = column[Int]("SECS")
  def distance = column[Double]("distance")

  def * = (id, hour, min, secs, distance)
}
