package net.iconhub.snippet

import net.liftmodules.openid.SimpleOpenIDVendor
import scala.xml.NodeSeq

class OpenId {

  def form(xhtml: NodeSeq) : NodeSeq = {
    SimpleOpenIDVendor.loginForm
  }
}