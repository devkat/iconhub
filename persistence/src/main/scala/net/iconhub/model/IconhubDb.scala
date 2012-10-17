package net.iconhub.model

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._

object IconhubDb extends Schema {
  
  val users = table[User]("Users")
  on(users)(u => declare(
      u.email is(unique, indexed)
  ))
  
  val roles = table[Role]

  val userToRole =
    manyToManyRelation(users, roles).
    via[UserToRole]((u, r, ur) => (ur.userId === u.id, ur.roleId === r.id)) 
      

  val icons = table[Icon]
  val iconSets = table[IconSet]
  val images = table[Image]
  

}