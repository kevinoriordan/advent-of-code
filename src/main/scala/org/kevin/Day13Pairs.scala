package org.kevin



import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object Day13Pairs extends App {

  val lines: List[String] = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("pairs.txt")).getLines().toList

  case class Packet(data: mutable.MutableList[Any])

  case class Pair(indexFromOne: Int, first: Packet, second: Packet)

  def toPair(tuples: Tuple2[Int, List[(String, Int)]]): Pair = {
    val pairInput = tuples._2.map(pair => (pair._1.trim, pair._2)).filter(_._1.nonEmpty).sortBy(_._2)

    val firstList = parsePacket(pairInput(0)._1)
    val secondList = parsePacket(pairInput(1)._1)
    Pair(tuples._1 + 1, Packet(firstList), Packet(secondList))
  }

  val pairs = lines.zipWithIndex.groupBy(_._2 / 3).map(toPair(_)).toList.sortBy(_.indexFromOne)

  val pairsGood = pairs.filter(x => compareList(x.first.data, x.second.data).getOrElse(true))


  println(s"Ans p1  ${pairsGood.map(_.indexFromOne).sum}")


  def compareList(firstList: mutable.MutableList[Any], secondList: mutable.MutableList[Any], checkLength: Boolean = true): Option[Boolean] = {
    var result: Option[Boolean] = None
    var break = false;
    val zipped = firstList.zip(secondList)
    val iter = zipped.iterator;
    while (!break && iter.hasNext) {
      val xy = iter.next()


      if (xy._1.isInstanceOf[mutable.MutableList[Any]]) {
        val firstAsList: mutable.MutableList[Any] = xy._1.asInstanceOf[mutable.MutableList[Any]]
        if (xy._2.isInstanceOf[mutable.MutableList[Any]]) {
          val secondAsList: mutable.MutableList[Any] = xy._2.asInstanceOf[mutable.MutableList[Any]]
          compareList(firstAsList, secondAsList) match {
            case Some(value) => {
              break = true
              result = Some(value)
            }
            case None => {}
          }

        }
        else {
          val secondAsInt = xy._2.asInstanceOf[Int]
          compareList(firstAsList, mutable.MutableList[Any](secondAsInt), checkLength = true) match {
            case Some(value) => {
              break = true
              result = Some(value)
            }
            case None => {}
          }
        }
      }
      else {
        val firstAsInt = xy._1.asInstanceOf[Int]
        if (xy._2.isInstanceOf[mutable.MutableList[Any]]) {
          val secondAsList: mutable.MutableList[Any] = xy._2.asInstanceOf[mutable.MutableList[Any]]
          compareList(mutable.MutableList[Any](firstAsInt), secondAsList, checkLength = true) match {
            case Some(value) => {
              break = true
              result = Some(value)
            }
            case None => {}
          }


        }
        else {
          val secondAsInt = xy._2.asInstanceOf[Int]
          if (firstAsInt > secondAsInt) {
            break = true
            result = Some(false)
          }
          else if( firstAsInt < secondAsInt)
            {
              break = true
              result = Some(true)
            }
        }

      }


    }

    result match {
      case Some(value) => Some(value)
      case None => if (firstList.size < secondList.size) Some(true) else if (firstList.size > secondList.size) Some(false) else None
    }

  }

  def correctOrder(x: Pair): Boolean = {

    val firstList = x.first
    val secondList = x.second
    compareList(firstList.data, secondList.data).getOrElse(true)
  }


  val ans = pairs.filter(x => correctOrder(x)).map(_.indexFromOne).sum


  def parseInt(char: Char, charIter: Iterator[Char]): (Int, Option[Char]) = {
    val chars = mutable.MutableList[Char](char)
    var stop = false
    var trailing: Option[Char] = None
    while (charIter.hasNext && !stop) {
      val nextChar = charIter.next()
      if (nextChar.isDigit) {
        chars += (nextChar)
      }
      else {
        stop = true
        trailing = Some(nextChar)

      }
    }
    val intChars = chars.mkString
    (intChars.toInt, trailing)

  }

  def parsePacket(input: String): mutable.MutableList[Any] = {
    val tokens = input.substring(1).replaceAll("\\s", "").split(",")
    val topList = mutable.MutableList[Any]()
    var stackOfList = ListBuffer[mutable.MutableList[Any]](topList)
    val tokenIter = tokens.iterator
    while (tokenIter.hasNext) {
      val token = tokenIter.next()
      val chars = token.toCharArray
      val charIter = chars.iterator
      while (charIter.hasNext) {
        val char = charIter.next
        if (char == '[') {
          val newList = mutable.MutableList[Any]()
          stackOfList.last += newList
          stackOfList.+=(newList)
        }
        else if (char == ']') {
          stackOfList.remove(stackOfList.size - 1)
        }
        else {
          val (intValue, nextChar) = parseInt(char, charIter)
          stackOfList.last.+=(intValue)
          nextChar match {
            case Some(finalChar) => {
              if (finalChar == '[') {
                val newList = mutable.MutableList[Any]()
                stackOfList.last += newList
                stackOfList.+=(newList)
              }
              else if (finalChar == ']') {
                stackOfList.remove(stackOfList.size - 1)
              }
            }
            case _ => {}

          }

        }
      }

    }
    topList

  }

}
