package org.kevin

import org.kevin.To.To

import scala.collection.mutable
import scala.io.Source


case class Point(x: Int, y: Int)

case class Position(order: Int, point: Point)

case class Move(direction: To, size: Int)

object Day9Rope extends App {

  val lines = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("rope.txt")).getLines().toList

  def toDirection(c: Char): To = {
    if (c == 'L') {
      To.Left
    }
    else if (c == 'R') {
      To.Right
    }
    else if (c == 'U') {
      To.Top
    }
    else {
      To.Bottom
    }
  }

  def parseLine(str: String): Move = {

    val tokens = str.trim.split("\\s")
    val direction = toDirection(tokens(0).charAt(0))
    val size = tokens(1).toInt
    Move(direction, size)
  }

  val moves = lines.map(parseLine(_))
  var headSeq: Int = 0
  var tailSeq: Int = 0
  val startHead = Position(headSeq, Point(0, 0))
  val startTail = Position(tailSeq, Point(0, 0))

  val headList = mutable.MutableList(startHead)
  val tailList = mutable.MutableList(startTail)

  def signedOne(nonZero: Int) = if (nonZero > 0) 1 else -1

  def moveTailIfNecessary(newHead: Position): Unit = {
    val currentTail = tailList.head
    val xDiff = newHead.point.x - currentTail.point.x
    val yDiff = newHead.point.y - currentTail.point.y

    if (xDiff == 0) {
      if (yDiff == 0) {
        // Nothing, overlapping
      }
      else if (Math.abs(yDiff) == 1) {
        // do nothing
      }
      else {

        val newTail = Position(currentTail.order + 1, Point(currentTail.point.x , currentTail.point.y + signedOne(yDiff)) )
        tailList.+=:(newTail)

      }
    }
    else if (yDiff == 0) {
      if (Math.abs(xDiff) > 1) {
        val newTail = Position(currentTail.order + 1, Point(currentTail.point.x + signedOne(xDiff), currentTail.point.y))
        tailList.+=:(newTail)
      }
    }
    else {
      // not on same row or column

      if (Math.abs(xDiff) >= 2 || Math.abs(yDiff) >= 2) {
        val newTail = Position(currentTail.order + 1, Point(currentTail.point.x + signedOne(xDiff), currentTail.point.y + signedOne(yDiff)))
        tailList.+=:(newTail)
      }


    }

   // println( s"movetail if necc: head ${headList.head}   tail ${tailList.head}")
  }

  def makeMove(move: Move): Unit = {
    move.direction match {
      case To.Left => {
        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x, cur.point.y - 1))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos)
        })
      }
      case To.Right => {
        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x, cur.point.y + 1))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos)
        })
      }
      case org.kevin.To.Top => {
        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x - 1, cur.point.y))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos)
        })
      }
      case org.kevin.To.Bottom
      => {

        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x + 1, cur.point.y))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos)
        })
      }

    }


  }


  moves.foreach(makeMove(_))

//  tailList.foreach(println(_))

  println(s"Ans1 = ${tailList.groupBy( _.point).toList.size}")
}