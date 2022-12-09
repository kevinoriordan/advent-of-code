package org.kevin

case class DirNode(dirName: String, path: List[String], dirs: List[Dir], files: List[File], dirNodes: List[DirNode] = List())
{
  def getPath(): String =
    {
      """/""" + path.drop(1).mkString( """/""" )
    }
}
