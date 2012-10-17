package net.iconhub.model

import org.squeryl.KeyedEntity

class Role(val name: String) extends KeyedEntity[Int] {
  val id: Int = 0
}

