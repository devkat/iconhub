package net.iconhub.model

import org.squeryl.Schema
import org.squeryl.annotations.Column
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.squerylrecord.KeyedRecord
import org.squeryl.Table

object IconhubDb extends Schema {
  
  val users = table[User]("users")
  val icons = table[Icon]("icon")
  val iconSets = table[IconSet]("iconset")
  val images = table[Image]("image")
  val roles = table[Role]("role")

  on(users)(u => declare(
      u.idField defineAs(autoIncremented("users_id_seq")),
      u.email defineAs(unique, indexed)
  ))
  
  for (tab <- tables if tab.isInstanceOf[Table[KeyedRecord[Long]]]) {
    val keyed = tab.asInstanceOf[Table[KeyedRecord[Long]]]
    on(keyed)(t => declare(
          t.idField defineAs(autoIncremented(tab.name + "_id_seq"))
    ))
  }
  
  val userToRole =
    manyToManyRelation(users, roles, "user_to_role").
    via[UserToRole]((u, r, ur) => (ur.userId === u.id, ur.roleId === r.id))

  val iconSetToIcon =
    manyToManyRelation(iconSets, icons, "iconset_to_icon").
    via[IconSetToIcon]((s, i, si) => (si.iconSetId === s.id, si.iconId === i.id))

}