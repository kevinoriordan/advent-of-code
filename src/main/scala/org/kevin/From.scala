package org.kevin

object From extends Enumeration {
  type From = Value
  val Left, Right, Top, Bottom = Value

  def convertToTo(from: From) = {
    from match {
      case Left => To.Right
      case Right => To.Left
      case Top => To.Bottom
      case Bottom => To.Top
    }
  }
}

