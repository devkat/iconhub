package net.iconhub.model

import scala.xml.NodeSeq
import java.sql.Timestamp
import java.util.Date
import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Column
import IconhubDb._

/**
 * The singleton that has methods for accessing the database
 */
class User extends KeyedEntity[Long] {
  
  val id: Long = 0
  val created: Timestamp = new Timestamp(System.currentTimeMillis)
  
  @Column
  var email: String = _
  @Column("password_hash")
  var passwordHash: Option[String] = _
  @Column //("password_salt")
  var passwordSalt: Option[Array[Byte]] = _

  lazy val roles = userToRole.left(this)

    /*
  @OneToMany
  var iconSets: java.util.List[IconSet] = new java.util.Vector[IconSet]
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column
  var created: java.util.Date

  
  protected def findUserByUniqueId(id: String): Box[TheUserType] = User.findById(id)
  
  protected def findUserByUserName(name: String): Box[TheUserType] = User.findByUserName(name)
  
  protected def createNewUserInstance(): TheUserType = new User
  
  protected def userFromStringId(id:String): Box[TheUserType] = Empty
  */
  
}

object User {
  
  
  def now = new Timestamp(System.currentTimeMillis)

  def findById(id:Long): Option[User] = users.lookup(id)
  
  def findByEmail(email:String): Option[User] =
    from(users)(u => where(u.email === email) select(u)).headOption
  
  def findByUsername(username:String): Option[User] = findByEmail(username)
}