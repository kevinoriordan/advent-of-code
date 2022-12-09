package org.kevin

import org.kevin.MainDir.{getClass, parseLine}

import scala.io.Source

object Day4Jobs extends App {

  val pairs = Source.fromInputStream( getClass.getClassLoader.getResourceAsStream( "jobranges.txt")).getLines().map(parseLine(_)).toList

  val subset = pairs.filter(fullyContains(_))
 // subset.foreach(println(_))
  println( subset.size)

  val notFullyContained = pairs.filter(!fullyContains(_)).filter( nonContainedOverlap(_))
 // notFullyContained.foreach(println(_))
  println( notFullyContained.size)


  val exclusive = pairs.filter(!fullyContains(_)).filter(!nonContainedOverlap(_))
  exclusive.foreach( println(_))


  def innerFullyContains(outer: Range, inner: Range): Boolean = {
    outer.start <= inner.start && outer.end >= inner.end;
  }

  def fullyContains(tuple: (Range, Range)): Boolean = {
    innerFullyContains(tuple._1, tuple._2)  || innerFullyContains(tuple._2, tuple._1)
  }

  def nonContainedOverlap(tuple: (Range, Range) ): Boolean = {
    if (tuple._1.end < tuple._2.start){
      false
    }
    else if ( tuple._1.end >= tuple._2.start && tuple._1.start <= tuple._2.end)
      {
        true
      }
    else if( tuple._1.end <= tuple._2.end)
      {
        true
      }
    else
      {
        false;
      }
  }




  def parseRange(input: String): Range = {
    val pair = input.trim.split("-")
    val start = pair(0).toInt
    val end = pair(1).toInt
    Range.inclusive(start, end)
  }

  def parseLine(line: String) = {
    val pair = line.trim.split(",");
    val firstRange = parseRange(pair(0));
    val secondRange = parseRange(pair(1))
    (firstRange, secondRange)
  }

}
