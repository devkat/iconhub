package net.iconhub.model

import org.squeryl.Table

trait TableRecord[T] {
  
  val table:Table[T]

}