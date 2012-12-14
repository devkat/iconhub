package net.iconhub.auth

import net.iconhub.model.Persistence
import net.iconhub.model.User
import net.iconhub.model.IconhubDb
import org.scalatest.FlatSpec
import org.apache.shiro.authc.credential.HashedCredentialsMatcher
import org.apache.shiro.crypto.hash.Sha256Hash
import net.iconhub.shiro.IconhubRealm
import org.apache.shiro.authc.UsernamePasswordToken
import net.liftweb.squerylrecord.RecordTypeMode._
import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.util.ByteSource

class AuthSpec extends Persistence {
  
  def toEq(left: Any) = convertToEqualizer(left: Any)
  
  val realm = new IconhubRealm
  val matcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME)
  matcher.setStoredCredentialsHexEncoded(false)
  matcher.setHashIterations(1)

  def createUser(email: String, password: String) = {
    val user = User.createRecord
    user.email(email)
    user.password(password)
    IconhubDb.users insert user
    user
  }
  
  def authenticates_?(user: User, password: String) = {
    val token = new UsernamePasswordToken(user.email.is, password)
    val info = realm.doGetAuthenticationInfo(token)
    matcher.doCredentialsMatch(token, info)
  }
  
  "A hash" should " validate" in {
    val email = "andreas@devkat.net"
    val password = "test123"
    val rng = new SecureRandomNumberGenerator
    val salt = rng.nextBytes().toString + email
    val hash = new Sha256Hash(password, salt).toBase64
    
    val token = new UsernamePasswordToken(email, password)
    val info = new SimpleAuthenticationInfo(email, hash, ByteSource.Util.bytes(salt), "realm")
    assert(matcher.doCredentialsMatch(token, info))
  }
  
  
  "A User" should "be able to authenticate" in withDb { () =>
    inTransaction {
      val email = "andreas@devkat.net"
      val password = "test123"
      val user = createUser(email, password)
      try {
        assert(authenticates_?(user, password))
        //assert(!authenticates_?(user, password + "FAIL"))
      }
      finally {
        IconhubDb.users delete user.id
      }
    }
  }

}