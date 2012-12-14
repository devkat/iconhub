package net.iconhub.model

import net.liftweb.record.Record
import net.liftweb.squerylrecord.KeyedRecord

abstract class MyRecord[R <: Record[R]] extends Record[R] with TableRecord[R] {
  self: R =>

}