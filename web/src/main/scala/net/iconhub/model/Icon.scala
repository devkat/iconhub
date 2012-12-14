package net.iconhub.model

import java.sql.Timestamp
import org.squeryl.annotations.Column
import net.liftweb.record.Record
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.record.field._
import net.liftweb.record.MetaRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import IconhubDb._

class Icon private() extends Record[Icon] with KeyedRecord[Long] {

  override def meta = Icon
  
  @Column(name="id")
  override val idField = new LongField(this)
    
  val slug = new StringField(this, 128)
  val name = new StringField(this, 256)
  val created = new DateTimeField(this)
  
  @Column(name="owner_id")
  val ownerId = new LongField(this);

  /*
  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[IconSet], fetch = FetchType.LAZY)
  var sets:java.util.List[IconSet] = new java.util.Vector[IconSet]

  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[Image], fetch = FetchType.LAZY)
  var images:java.util.List[Image] = new java.util.Vector[Image]
  */

}

object Icon extends Icon with MetaRecord[Icon] {
  
  def findBySlug(userId: Long, slug: String) =
    from(icons)(i => where(
        i.ownerId === userId
        and i.slug.get === slug)
    select(i)).headOption
}