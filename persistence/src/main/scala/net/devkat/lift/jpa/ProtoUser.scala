package net.devkat.lift.jpa

import javax.persistence.Column
import net.liftweb.proto.{ProtoUser => DefProtoUser}
import net.liftweb.common._
import scala.xml.NodeSeq
import net.liftweb.util.BaseField
import net.liftweb.util.StringHelpers._
import net.liftweb.util.FieldError

trait ProtoUser extends IdPk with DefProtoUser {

  type TheUserType = ProtoUser
  
  @Column(nullable = false)
  var uniqueId:String = _
  
  @Column(nullable = false)
  var email:String = _
  
  @Column
  var firstName:String = _
  
  @Column
  var lastName:String = _
  
  @Column
  var superUser:Boolean = false
  
  @Column
  var validated:Boolean = false
  
  @Column
  var password:String = _
  
  def editFields: List[FieldPointerType] = Nil
  
  def signupFields: List[FieldPointerType] = Nil

  /**
   * Given a field pointer and an instance, get the field on that instance
   */
  protected def computeFieldFromPointer(instance: TheUserType, pointer: FieldPointerType): Box[BaseField]
    = Empty

  /**
   * Based on a FieldPointer, build a FieldPointerBridge
   */
  protected implicit def buildFieldBridge(from: FieldPointerType): FieldPointerBridge
    = new MyPointer(from)

  protected class MyPointer(from: FieldPointerType) extends FieldPointerBridge {
    def displayHtml: NodeSeq = from.displayHtml
    def isPasswordField_? : Boolean = false
  }

  /**
   * Convert an instance of TheUserType to the Bridge trait
   */
  protected implicit def typeToBridge(in: TheUserType): UserBridge = 
    new MyUserBridge(in)

  /**
   * Bridges from TheUserType to methods used in this class
   */
  protected class MyUserBridge(in: TheUserType) extends UserBridge {
    /**
     * Convert the user's primary key to a String
     */
    def userIdAsString: String = in.id.toString

    /**
     * Return the user's first name
     */
    def getFirstName: String = in.firstName

    /**
     * Return the user's last name
     */
    def getLastName: String = in.lastName

    /**
     * Get the user's email
     */
    def getEmail: String = in.email

    /**
     * Is the user a superuser
     */
    def superUser_? : Boolean = in.superUser

    /**
     * Has the user been validated?
     */
    def validated_? : Boolean = in.validated

    /**
     * Does the supplied password match the actual password?
     */
    def testPassword(toTest: Box[String]): Boolean =
      (for (pw <- toTest) yield pw == in.password) openOr false
      //toTest.map(in.password.match_?) openOr false

    /**
     * Set the validation flag on the user and return the user
     */
    def setValidated(validation: Boolean): TheUserType = {
      in.validated = validation
      in
    }

    /**
     * Set the unique ID for this user to a new value
     */
    def resetUniqueId(): TheUserType = {
      in.uniqueId = randomString(10)
      in
    }

    /**
     * Return the unique ID for the user
     */
    def getUniqueId(): String = in.uniqueId

    /**
     * Validate the user
     */
    def validate: List[FieldError] = in.validate

    /**
     * Given a list of string, set the password
     */
    def setPasswordFromListString(pwd: List[String]): TheUserType = {
      setPasswordFromAny(pwd)
      in
    }
    
    def setPasswordFromAny(pwd: Any): Box[String] = {
        pwd match {
          case (a: Array[String]) if (a.length == 2 && a(0)   == a(1)) => in.password = a(0); Full(a(9))
          case (h1: String) :: (h2: String) :: Nil if h1 == h2 => in.password = h1; Full(h1)
          case (hash: String) if(hash.startsWith("$2a$")) => in.password = hash; Full(hash)
          case Full(hash: String) if(hash.startsWith("$2a$")) => in.password = hash; Full(hash)
          case _ => Failure("Passwords do not match")
        }
    }

    /**
     * Save the user to backing store
     */
    def save(): Boolean = { Model.newEM.persist(in); true }
  }

}