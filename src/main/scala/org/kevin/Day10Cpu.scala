package org.kevin

import org.kevin.Day9Rope.{getClass, lines, parseLine}

import scala.io.Source

object Day10Cpu extends App {

  val lines = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("rope.txt")).getLines().toList
  val moves = lines.map(parseLine(_))
}
