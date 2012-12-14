package net.iconhub.snippet

import scala.xml.NodeSeq
import net.iconhub.model.User

class Auth {
  
  def loggedIn(xhtml: NodeSeq) : NodeSeq =
    if (User.loggedIn_?) xhtml else Nil
    
  def notLoggedIn(xhtml: NodeSeq) : NodeSeq =
    if (User.loggedIn_?) Nil else xhtml

}