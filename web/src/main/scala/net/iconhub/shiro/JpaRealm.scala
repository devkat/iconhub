package net.iconhub.shiro

import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.util.SimpleByteSource
import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.UsernamePasswordToken
import net.iconhub.model.User
import org.apache.shiro.authc.SimpleAuthenticationInfo
import net.liftweb.common._
import org.apache.shiro.util.ByteSource
import org.apache.shiro.authz.SimpleAuthorizationInfo

/**
 * http://code.google.com/p/osu/source/browse/trunk/click-orm-integration/orm/src/main/java/org/apache/click/extras/security/shiro/JpaRealm.java?spec=svn131&r=131
 */
class JpaRealm extends AuthorizingRealm {
  
  val rng = new SecureRandomNumberGenerator
  def newSalt = rng.nextBytes()
  
  def doGetAuthenticationInfo(token:AuthenticationToken) = {
    val userToken = token.asInstanceOf[UsernamePasswordToken]
    User.findByUsername(userToken.getUsername()) match {
      case Full(user) => {
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
      case Full(user) => {
        val info = new SimpleAuthorizationInfo
        for (Role role : user.getRoles()) {
            info.addRole(role.getName());
            info.addStringPermissions(role.getPermissions());
        }
        return info;
      }
      case _ => null
    }
    val user = 
  }
  
  def createUserAccount(username: String, password: String) {
    val salt = newSalt
    val hash = new Sha256Hash(password, (new SimpleByteSource(newSalt + username)).getBytes).toBase64
    val user = new User
    user.email = username
    user.passwordHash = hash
    user.passwordSalt = salt.getBytes()
  }
  
}