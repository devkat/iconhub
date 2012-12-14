package net.iconhub.model

import net.liftweb.record.TypedField
import net.liftweb.util.FieldError
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.Table
import net.liftweb.record.field._
import net.liftweb.record._
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.common._
import net.liftweb.http.S

//trait Unique[R <: Record[R] with KeyedRecord[_] with TableRecord[R]]
trait Unique[R <: MyRecord[R]]
        extends StringTypedField with OwnedField[R] {

  def option(b:Box[Field[_, R]]): Option[StringTypedField] =
    b.asInstanceOf[Box[StringTypedField]] match {
    case Full(f) => Some(f)
    case _ => Empty
  }

  def unique(msg:String)(value:Unique.this.ValueType): List[FieldError] = {
    val v = value.asInstanceOf[String]
    (from(this.owner.table)
         (record => where(option(record.fieldByName(this.name)) === v)
             compute(count)):Long)
    match {
      case 0 => Nil
      case _ => FieldError(this, msg) :: Nil
    }
  }
  
  override def validations = unique(S.?(this.name + "-not-unique")) _ :: super.validations
  
}