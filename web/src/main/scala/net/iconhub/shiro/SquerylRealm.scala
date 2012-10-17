package net.iconhub.shiro

import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.util.SimpleByteSource
import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.UsernamePasswordToken
import net.iconhub.model._
import org.apache.shiro.authc.SimpleAuthenticationInfo
import net.liftweb.common._
import org.apache.shiro.util.ByteSource
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.squeryl.PrimitiveTypeMode._

class SquerylRealm extends AuthorizingRealm {
  
  import IconhubDb._

  val rng = new SecureRandomNumberGenerator
  def newSalt = rng.nextBytes()
  
  def doGetAuthenticationInfo(token:AuthenticationToken) = {
    val userToken = token.asInstanceOf[UsernamePasswordToken]
    User.findByUsername(userToken.getUsername()) match {
      case Some(user) => {
        val info = new SimpleAuthenticationInfo(user.id, user.passwordHash, getName())
        info.setCredentialsSalt(ByteSource.Util.bytes(user.passwordSalt))
        info
      }
      case _ => null
    }
  }
  
  def doGetAuthorizationInfo(principals:PrincipalCollection) : AuthorizationInfo = {
    val userId = principals.fromRealm(getName()).iterator.next.asInstanceOf[Long]
    User.findById(userId) match {
      case Some(user) => {
        val info = new SimpleAuthorizationInfo
        /*
        for (Role role : user.getRoles()) {
            info.addRole(role.getName());
            info.addStringPermissions(role.getPermissions());
        }
        */
        return info;
      }
      case _ => null
    }
  }
  
  def createUserAccount(username: String, password: String) {
    val salt = newSalt
    val hash = new Sha256Hash(password, (new SimpleByteSource(newSalt + username)).getBytes).toBase64
    val user = new User
    user.email = username
    user.passwordHash = Some(hash)
    user.passwordSalt = Some(salt.getBytes())
  }
  
}