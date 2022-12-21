package org.kevin

import org.kevin.Day16Valves.states

import scala.collection.mutable
import scala.io.Source

case class State(valve: Valve, openValues: mutable.Map[String, Int], totalSoFar: Int = 0)
case class Valve(name: String, rate: Int, nodeNames: List[String]) {
}

object Day16Valves extends App {

  val lines = Source.fromInputStream(getClass.getClassLoader().getResourceAsStream("./valves.txt")).getLines()
  val nextToValve: mutable.Map[String, Valve] = mutable.Map[String, Valve]()

  /** Valve EU has flow rate=10; tunnels lead to valves DX, UW, RY, NC */
  def parseLine(input: String) = {
    val tokens = input.replaceAll(",", "").replaceAll(";", "").split("""\s""")
    val valveName = tokens(1)
    val flowRate = tokens(4).split("=")(1).toInt
    val nextValves = tokens.toList.drop(9)

    Valve(valveName, flowRate, nextValves)


  }

  val nodes = lines.map(line => parseLine(line.trim())).toList
  nodes.foreach(node => {
    nextToValve(node.name) = node
  })

  var states = mutable.Set[State]()
  states += State( nodes.find( _.name == "AA").get, mutable.Map[String,Int]())
  (0 to 29).foreach(_ => {
    println("Interating..")
    val newStates = mutable.Set[State]()
    states.foreach(
      s => {
        val flow = s.openValues.map(_._2).sum + s.totalSoFar
        if (!s.openValues.contains(s.valve.name) && s.valve.rate > 0) {
          val newOpenValues = mutable.Map[String, Int]() ++= s.openValues
          nextToValve.get(s.valve.name).map(v => newOpenValues.put(s.valve.name, v.rate))
          val newState = State(s.valve, newOpenValues, flow)
          newStates += newState
        }
        val adjacent = s.valve.nodeNames.map(name => nextToValve.get(name)).flatten.map(v => new State(v, s.openValues, flow))
        newStates ++= adjacent
      })
    states =newStates
  })

  println(s"Ans p1 ${states.toList.map( _.totalSoFar).max}")
}
