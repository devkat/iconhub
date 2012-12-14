package net.iconhub.model

import org.squeryl.annotations.Column
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.record.Record
import net.liftweb.record.field._
import net.liftweb.record.MetaRecord

class Image private() extends Record[Image] with KeyedRecord[Long] {
  
  override def meta = Image
    
  @Column(name="id")
  override val idField = new LongField(this)
  
  @Column(name="mime_type")
  val mimeType = new StringField(this, 256)
  val width = new IntField(this)
  val height = new IntField(this)
  val content = new BinaryField(this)
  
  @Column(name="icon_id")
  val iconId = new LongField(this)
  
}

object Image extends Image with MetaRecord[Image] {
  
}
