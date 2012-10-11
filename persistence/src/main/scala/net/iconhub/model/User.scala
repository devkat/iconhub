package net.iconhub.model

import javax.persistence._

/**
 * The singleton that has methods for accessing the database
 */
@Entity
@Table(name = "users")
class User extends IdPk {
  
  @Column(nullable = false)
  var email:String = _
  
  @OneToMany
  var iconSets : java.util.List[IconSet] = new java.util.Vector[IconSet]
  
}
