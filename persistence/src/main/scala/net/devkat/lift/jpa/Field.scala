package net.devkat.lift.jpa

import net.liftweb.util._
import net.liftweb.common._
import scala.xml.NodeSeq

class JpaField extends BaseField {
  
  var value:ValueType = _
  
  type ValidationFunction = ValueType => List[FieldError]

  /**
   * Generate a form control for the field
   */
  def toForm: Box[NodeSeq] = Full(<div/>)
  
  def validate: List[FieldError] = Nil

  def validations: List[ValidationFunction] = Nil
  
  def setFilter: List[ValueType => ValueType] = Nil
  
  def set(in: ValueType): ValueType = in
  
  def get: ValueType = value
  
  def name: String = ""
    
  def is:ValueType = value

}