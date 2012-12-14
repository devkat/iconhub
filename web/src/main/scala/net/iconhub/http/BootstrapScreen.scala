package net.iconhub.http

import net.liftweb.http._
import scala.xml._

abstract class BootstrapScreen extends LiftScreen {
  
  override def additionalAttributes(): MetaData =
    new UnprefixedAttribute("class", "form-horizontal", Null)

  override def cancelButton: Elem =
    <button class="btn">{S.?("Cancel")}</button>

  override def finishButton: Elem =
    <button class="btn btn-primary">{S.?("Finish")}</button>
  
}