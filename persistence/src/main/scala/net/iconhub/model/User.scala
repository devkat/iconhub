package net.iconhub.model

import javax.persistence._
import net.liftweb.common.Box
import net.liftweb.common.Full
import net.liftweb.util.BaseField
import net.liftweb.common.Empty
import org.scala_libs.jpa.LocalEMF
import scala.xml.NodeSeq
import net.devkat.lift.jpa.Model
import net.devkat.lift.jpa.IdPk
import net.liftweb.util.FieldError
import javax.persistence.NamedQueries

/**
 * The singleton that has methods for accessing the database
 */
@Entity
@Table(name = "users")
@NamedQueries(Array(
    new NamedQuery(name="User.findById", query="SELECT u FROM User u WHERE u.id = :id"),
    new NamedQuery(name="User.findByEmail", query="SELECT u FROM User u WHERE u.email = :email")
))
class User extends IdPk {
  
  @Column(nullable = false)
  var email:String = _
  
  @OneToMany
  var iconSets : java.util.List[IconSet] = new java.util.Vector[IconSet]
  
  def validate: List[FieldError] = Nil
  
  /**
   * Has the user been validated?
   */
  def validated_? : Boolean = false

  
  /*
  protected def findUserByUniqueId(id: String): Box[TheUserType] = User.findById(id)
  
  protected def findUserByUserName(name: String): Box[TheUserType] = User.findByUserName(name)
  
  protected def createNewUserInstance(): TheUserType = new User
  
  protected def userFromStringId(id:String): Box[TheUserType] = Empty
  */
  
}

object User extends User {
  
  def findById(id:String): Box[User] = {
    val query = Model.newEM.createNamedQuery("User.findById", ("id", id))
    query.findOne
  }
  
  def findByEmail(email:String): Box[User] = {
    val query = Model.newEM.createNamedQuery("User.findByEmail", ("email", email))
    query.findOne
  }
  
}