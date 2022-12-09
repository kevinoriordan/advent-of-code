package org.kevin

case class DirTotal(size: Long, name: String)


import scala.io.Source

sealed abstract class Line {

}

abstract class Input() extends Line() {

}

case class Cd(dir: String) extends Input()

case class Ls() extends Input()

abstract class Output() extends Line()

case class Dir(dir: String) extends Output

case class File(name: String, size: Long) extends Output


object MainDir extends App {


  def parseInput(str: String): Input = {

    val tokens = str.trim().split("""\s""").toList
    if (tokens(0) == "cd") {
      Cd(tokens(1))
    }
    else {
      Ls()
    }

  }


  def parseOutput(str: String): Output = {
    val tokens = str.trim().split("""\s""").toList
    if (tokens(0) == "dir") {
      Dir(tokens(1))
    }
    else {
      File(tokens(1), tokens(0).toLong)
    }
  }

  def parseLine(str: String): Line = {
    if (str.startsWith("""$""")) {
      parseInput(str.substring(1))
    }
    else {
      parseOutput(str.trim)
    }

  }

  val list = Source.fromInputStream( getClass.getClassLoader.getResourceAsStream( "dir.txt")).getLines().map(parseLine(_)).toList
  list.foreach(println _)

  // Know first two lines
  //val currentDirName = """\"""

  var outputMode: Boolean = false;
  var currentDirs: List[Dir] = List();
  var currentFiles: List[File] = List();

  var starting = true;

  var rootNode: Option[String] = None
  var currentNode: Option[DirNode] = None

  val dirNameToDirNode = scala.collection.mutable.Map[String, DirNode]()
  var parentNode: Option[String] = None

  var currentPath: List[String] = List()

  def handle(line: Line) = {
    line match {
      case input: Input => {
        if (outputMode) {
          outputMode = false;
          val newDirNode: DirNode = createNode
          currentDirs = List()
          currentFiles = List()

          if (currentPath.size >= 2) {
            val parent = """/""" + currentPath.dropRight(1).drop(1).mkString("""/""")
            val par = dirNameToDirNode(parent)
            val newDirNodes = newDirNode :: par.dirNodes
            dirNameToDirNode(parent) = par.copy(dirNodes = newDirNodes)

          }

        }
        input match {
          case Cd(dir) => {
            if (dir == "..") {
              if (currentPath.size > 1) {
                currentPath = currentPath.dropRight(1)
              }
            }
            else {
              currentPath = currentPath :+ dir
            }
          }
          case Ls() => {}
          case _ => ???
        }

      }
      case output: Output => {
        if (outputMode == false) {
          outputMode = true
        }
        output match {
          case dir: Dir => {
            currentDirs = dir :: currentDirs
          }
          case file: File => {
            currentFiles = file :: currentFiles
          }
        }
      }
    }
  }

  private def createNode = {
    val currentDirName = currentPath.takeRight(1)(0)
    val newDirNode = DirNode(currentDirName, currentPath, currentDirs, currentFiles)
    dirNameToDirNode(newDirNode.getPath()) = newDirNode
    newDirNode
  }

  list.foreach(handle(_))
  createNode

  val root = dirNameToDirNode("""/""")
  println(dirNameToDirNode("""/"""))
  val computed = scala.collection.mutable.Map[String, DirTotal]();

  def getAllDirectoriesMatching(current: DirNode, totals: List[DirTotal]): List[DirTotal] = {
    var extra = List[DirTotal]()
    val dirTotal = calculateSize(current)
    if (dirTotal.size <= 100000) {
      extra = dirTotal :: extra
    }
    val refreshed = current.dirs.map(x => getChildPath(current, x)).map(dirNameToDirNode(_))
    val children = refreshed.flatMap(getAllDirectoriesMatching(_, List()))
    extra ++ children ++ totals

  }


  private def getChildPath(current: DirNode, x: Dir) = {
    val currentPath = current.getPath();
    val res = if (currentPath != """/""") {
     List[String](current.getPath(), x.dir).mkString("""/""")
    }
    else {
      currentPath + x.dir
    }
    res
  }

  def calculateSize(node: DirNode): DirTotal = {
    println(s"calc size ${node.dirName}")
    computed.get(node.getPath()) match {
      case Some(value) => value
      case None => {
        val filesSize = node.files.map(_.size).sum
        val refreshed = node.dirs.map(x => getChildPath(node, x)).map(dirNameToDirNode(_))


        val kidsSize = refreshed.map(calculateSize(_)).map(_.size).sum
        val totalSize = filesSize + kidsSize
        val result = DirTotal(totalSize, node.dirName)
        computed(node.getPath()) = result;
        result
      }
    }
  }

  val ans = getAllDirectoriesMatching(root, List[DirTotal]())
  ans.foreach(println(_))

  println(ans.map(_.size).sum)


  var maxSizeAfterDelete = 0l
  var directorySize = 0l;

  def check(tuple: (String, DirTotal) ): Unit =
  {
    val sizeAfterDelete = 43441553 - tuple._2.size
    if( sizeAfterDelete> maxSizeAfterDelete && sizeAfterDelete <= 40000000)
      {
          directorySize = tuple._2.size
         println("MAX" +  tuple)
        maxSizeAfterDelete = sizeAfterDelete
      }
  }

  computed.foreach( check(_))
  println(directorySize)
}
