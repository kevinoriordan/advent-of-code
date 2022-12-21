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

  val startHead = Position(0, Point(0, 0))
  val startTail = Position(0, Point(0, 0))

  val headList = mutable.MutableList(startHead)
  val tailList = mutable.MutableList(startTail)

  def signedOne(nonZero: Int) = if (nonZero > 0) 1 else -1

  def moveTailIfNecessary(newHead: Position, tails: List[mutable.MutableList[Position]]): Unit = {
    def innerMove(currentHead: Position, tList: mutable.MutableList[Position]): Boolean = {
      val currentTail = tList.head
      val xDiff = currentHead.point.x - currentTail.point.x
      val yDiff = currentHead.point.y - currentTail.point.y

      if (xDiff == 0) {
        if (yDiff == 0) {
          // Nothing, overlapping
          false
        }
        else if (Math.abs(yDiff) == 1) {
          // do nothing
          false
        }
        else {

          val newTail = Position(currentTail.order + 1, Point(currentTail.point.x, currentTail.point.y + signedOne(yDiff)))
          tList.+=:(newTail)
          true

        }
      }
      else if (yDiff == 0) {
        if (Math.abs(xDiff) > 1) {
          val newTail = Position(currentTail.order + 1, Point(currentTail.point.x + signedOne(xDiff), currentTail.point.y))
          tList.+=:(newTail)
          true
        }
        else {
          false
        }
      }
      else {
        // not on same row or column

        if (Math.abs(xDiff) >= 2 || Math.abs(yDiff) >= 2) {
          val newTail = Position(currentTail.order + 1, Point(currentTail.point.x + signedOne(xDiff), currentTail.point.y + signedOne(yDiff)))
          tList.+=:(newTail)
          true
        }
        else {
          false
        }


      }
    }

    val iter = tails.iterator
    var currentHead = newHead
    var moved = true;
    while (iter.hasNext) {
      val tails = iter.next()
      if (moved) {
        moved = innerMove(currentHead, tails)
      }
      currentHead = tails.head
    }


    // println( s"movetail if necc: head ${headList.head}   tail ${tailList.head}")
  }

  def makeMove(move: Move, tails: List[mutable.MutableList[Position]]): Unit = {
    move.direction match {
      case To.Left => {
        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x, cur.point.y - 1))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos, tails)
        })
      }
      case To.Right => {
        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x, cur.point.y + 1))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos, tails)
        })
      }
      case org.kevin.To.Top => {
        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x - 1, cur.point.y))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos, tails)
        })
      }
      case org.kevin.To.Bottom
      => {

        val range = 1 to move.size
        range.foreach(x => {
          val cur = headList.head
          val newPos = Position(cur.order + 1, Point(cur.point.x + 1, cur.point.y))
          headList.+=:(newPos)
          moveTailIfNecessary(newPos, tails)
        })
      }

    }


  }


 // moves.foreach(makeMove(_, List(tailList)))

  //  tailList.foreach(println(_))

 // println(s"Ans1 = ${tailList.groupBy(_.point).toList.size}")


  val tails = (0 to 8).map(x => mutable.MutableList(startTail)).toList

  moves.foreach(makeMove(_, tails))

  tails.foreach(x => println(x.groupBy(_.point).toList.size))

  println(s"Ans1 = ${tails.head.groupBy(_.point).toList.size}")

  println(s"Ans2 = ${tails.last.groupBy(_.point).toList.size}")

}