package net.iconhub.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.iconhub.model._
import net.iconhub.shiro.IconhubRealm
import net.iconhub.http.{BootstrapScreen => Screen}
import scala.xml._
import net.iconhub.model
import net.liftweb.common._
import net.liftweb.util.FieldError

object Signup extends Screen {
  
  object user extends ScreenVar(User.createRecord)
  
  override def screenTop = <b>Sign up</b>
    
  addFields(() => user.is.email)
  
  val password = new Field {
    type ValueType = String
    override def name = S ? "password"
    override implicit def manifest = buildIt[String]
    override def default = ""
    override def toForm: Box[NodeSeq] = SHtml.password(is, set _)
    def validatePassword(pw:String): List[FieldError] =
      if (pw.length() >= 6) Nil else S ? "password.too.short"
    override def validations = validatePassword _ :: super.validations
  }
  
  val repeatPassword = new Field {
    type ValueType = String
    override def name = S ? "repeat.password"
    override implicit def manifest = buildIt[String]
    override def default = ""
    override def toForm: Box[NodeSeq] = SHtml.password(is, set _)
    def comparePasswords(pw:String): List[FieldError] =
      if (password.is == pw) Nil else S ? "passwords.do.not.match"
    override def validations = comparePasswords _ :: super.validations
  }
  //val repeatPassword = repeatPasswordField(S ? "Repeat password", "")
    
    //field(S ? "Repeat password", "")
  
  /*
  override def validations = comparePasswords _ :: super.validations
  
  def comparePasswords(): Errors =
    if (password == repeatPassword) Nil else S ? "Passwords don't match."
    */
    
  /*
  val email = field(S ? "E-Mail", "")
  */
    
  // capture from whence the user came so we
  // can send them back
  private val whence = S.referer openOr "/"
  
  def finish() {
    user.password(password)
    IconhubDb.users.insert(user)
    S.redirectTo(whence)
  }

  
  /*
  def dispatch = {case "render" => render}
  
  def render =
    "name=email" #> SHtml.text(email, email = _, "id" -> "the_email") &
    "name=password" #> SHtml.password(password, password = _) &
    "name=repeatPassword" #> SHtml.password(repeatPassword, repeatPassword = _) &
    "type=submit" #> SHtml.onSubmitUnit(process)
    
  private def process() = {
    for (
      r <- S.request if S.post_?,
      email <- 
    ) yield S.redirectTo(whence)
  }
  */

}