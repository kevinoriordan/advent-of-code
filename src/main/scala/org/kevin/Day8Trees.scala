package org.kevin

import org.kevin
import org.kevin.From.From
import org.kevin.To.To

import scala.collection.mutable
import scala.io.Source


class Visible() {
  def setVisibleFrom(from: From): Unit = {
    from match {
      case From.Left => fromLeft = true
      case From.Right => fromRight = true
      case org.kevin.From.Top => fromTop = true
      case org.kevin.From.Bottom => fromBottom = true
    }


  }

  var fromLeft: Boolean = false
  var fromRight: Boolean = false
  var fromTop: Boolean = false
  var fromBottom: Boolean = false

  var viewToLeft: Option[Int] = None
  var viewToRight: Option[Int] = None
  var viewToBottom: Option[Int] = None
  var viewToTop: Option[Int] = None

  def isVisible(): Boolean = fromLeft || fromRight || fromTop || fromBottom


  def getVisibleScore() = viewToLeft.getOrElse(0) * viewToRight.getOrElse(0) * viewToTop.getOrElse(0) * viewToBottom.getOrElse(0)

  def isVisible(to: To): Boolean = {
    to match {
      case To.Left => fromRight
      case To.Right => fromLeft
      case To.Top => fromBottom
      case To.Bottom => fromTop
    }
  }
}

case class Tree(height: Int, x: Int, y: Int, visible: Visible)


object Day8Trees extends App {

  def parseLine(str: String, rowIndex: Int): List[Tree] = {
    val chars = str.trim.toCharArray
    (chars.zipWithIndex).map(pair => {
      //  println(pair._1 + " " + pair._1.toInt)
      Tree(pair._1.asDigit, rowIndex, pair._2, new Visible())

    }).toList
  }

  val lines = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("trees.txt")).getLines().toList
  val list = (lines zipWithIndex).map(pair => parseLine(pair._1, pair._2)).toList
  list.foreach(setVisibility(_, From.Left))
  list.map(_.reverse).foreach(setVisibility(_, From.Right))

  def getIndexFromEach(i: Int, list: List[List[Tree]]): List[Tree] = {
    list.map(_ (i))
  }

  val cols = (0 to list.head.size - 1).map(getIndexFromEach(_, list)).toList

  cols.foreach(setVisibility(_, From.Top))
  cols.map(_.reverse).foreach(setVisibility(_, From.Bottom))


  def setVisibility(trees: List[Tree], from: From): Unit = {
    var maxSoFar = 0;

    def checkVis(tree: Tree, from: From): Unit = {
      if (tree.height > maxSoFar) {
        tree.visible.setVisibleFrom(from)
        maxSoFar = tree.height
      }

    }

    trees.foreach(checkVis(_, from))

  }

  var visibleCount = 0;

  def checkTreeVis(tree: Tree): Unit = {
    if (tree.visible.isVisible())
      visibleCount = visibleCount + 1
  }

  list.flatMap(x => x).foreach(checkTreeVis(_))


  println(s"""Ans part one $visibleCount""")


  // to right = from left

  def processWithExternalView(tree: Tree, to: kevin.To.Value, size: Int): Unit = {
    to match {
      case To.Left => {
        val view = tree.y
        tree.visible.viewToLeft = Some(view)
      }
      case To.Right => {

        val view = size - 1 - tree.y
        tree.visible.viewToRight = Some(view)

      }
      case org.kevin.To.Top => {

        val view = tree.x
        tree.visible.viewToTop = Some(view)

      }
      case org.kevin.To.Bottom => {

        val view = size - 1 - tree.x
        tree.visible.viewToBottom = Some(view)

      }
    }
  }


  def processView(blocked: mutable.MutableList[Tree], taller: Tree, to: kevin.To.Value): Unit = {
    to match {
      case To.Left => {
        blocked.map(tree => {
          val view = (taller.y - tree.y) * -1
          tree.visible.viewToLeft = Some(view)
        })
      }
      case To.Right => {
        blocked.map(tree => {
          val view = taller.y - tree.y
          tree.visible.viewToRight = Some(view)
        })
      }
      case org.kevin.To.Top => {
        blocked.map(tree => {
          val view = (taller.x - tree.x) * -1
          tree.visible.viewToTop = Some(view)
        })
      }
      case org.kevin.To.Bottom => {
        blocked.map(tree => {
          val view = (taller.x - tree.x)
          tree.visible.viewToBottom = Some(view)
        })
      }
    }
  }

  def setView(trees: List[Tree], from: kevin.From.Value): Unit = {
    val inProgress = new mutable.TreeMap[Int, mutable.MutableList[Tree]]()
    val to = From.convertToTo(from)

    def calcNode(tree: Tree): Unit = {
      val current = tree.height
      val iter = inProgress.iterator;
      var anyLeft = !iter.isEmpty
      if (anyLeft) {
        while (anyLeft) {
          val next = iter.next()
          if (next._1 <= current) {
            processView(next._2, tree, to)
            next._2.clear()
          }
          anyLeft = !iter.isEmpty
        }

        //}
      }
      val list = inProgress.get(tree.height)
      list match {
        case Some(value) => value += tree
        case None => inProgress(tree.height) = mutable.MutableList[Tree](tree)
      }


    }

    trees.foreach(calcNode(_))
    inProgress.values.flatMap( x=> x).foreach( processWithExternalView(_,to, trees.size))
  }

  list.foreach(setView(_, From.Left))
  list.map(_.reverse).foreach(setView(_, From.Right))
  cols.foreach(setView(_, From.Top))
  cols.map(_.reverse).foreach(setView(_, From.Bottom))

  var maxVisibleScore = 0;
  var maxVisTree: Option[Tree] = None

  def checkTreeVisScore(tree: Tree): Unit = {
    val score = tree.visible.getVisibleScore()
    if (score > maxVisibleScore) {
      maxVisibleScore = score
      maxVisTree = Some(tree)

    }
  }

  list.flatMap(x => x).foreach(checkTreeVisScore(_))

  println(s"""Ans part two ${maxVisTree}  with vis score ${maxVisTree.get.visible.getVisibleScore()} and vis ${maxVisTree.get.visible}""")


}
