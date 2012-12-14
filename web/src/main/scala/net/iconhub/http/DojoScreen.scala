package net.iconhub.http

import net.liftweb.http._
import scala.xml._
import net.liftweb.record.field.StringField
import net.liftweb.common._

abstract class DojoScreen extends LiftScreen {
  
  override def cancelButton: Elem = button(S.?("Cancel"))

  override def finishButton: Elem = button(S.?("Finish"))
  
  def button(text:String): Elem =
    <button data-dojo-type="dijit/form/Button">{text}</button>

  /*
  override def bindFields(xhtml: NodeSeq): NodeSeq = {
    xhtml % 
  }
  */
}
