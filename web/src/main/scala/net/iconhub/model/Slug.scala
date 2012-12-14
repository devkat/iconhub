package net.iconhub.model

import net.liftweb.record.field.StringField
import net.liftweb.record.Record

class Slug[T <: MyRecord[T]](owner:T) extends StringField(owner, 128) with Unique[T] {

}