package net.iconhub.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.dsl.CompositeKey2
import org.squeryl.PrimitiveTypeMode._

class UserToRole(
    @Column("user_id")
    val userId: Long,
    @Column("role_id")
    val roleId: Int)
  extends KeyedEntity[CompositeKey2[Long, Int]] {
  def id = compositeKey(userId, roleId)
}
      