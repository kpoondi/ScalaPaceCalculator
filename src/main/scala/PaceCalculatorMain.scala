import scala.io.StdIn._

class PaceCalculatorMain {

  def resolvePace(paceDouble :Double) :Int = {
    paceDouble match {
      case x if x < 60 => 0
      case _ => 1 + resolvePace(paceDouble - 60)
    }
  }

  def run() :Unit = {
    println("This is a simple pace calculator")

    print("Enter number of hours: ")
    val hours = readInt()
    print("Enter number of minutes: ")
    val minutes = readInt()
    print("Enter number of seconds: ")
    val seconds = readInt()

    print("Enter total distance (miles): ")
    val miles = readDouble()

    val totalMins = hours * 60 + minutes + (seconds/60)

    val doublePace = totalMins/miles

    val paceHour = resolvePace(doublePace)
    val paceMin = (doublePace - (paceHour * 60)).toInt
    val paceSec = (((doublePace - (paceHour * 60)) - (paceMin)) * 60).toInt

    println(s"Your pace was ${paceHour}h${paceMin}m${paceSec}s per mile")
  }
}

object PaceCalculatorMain extends PaceCalculatorMain with App {
  run()
}
