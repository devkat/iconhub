package net.iconhub.model

object Role extends Enumeration with Enumv {
  val Admin = Value("admin", "Administrator")
  val User = Value("user", "User")
}

class RoleType extends EnumvType(Role) {
}