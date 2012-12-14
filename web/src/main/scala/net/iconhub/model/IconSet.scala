package net.iconhub.model

import java.sql.Timestamp
import org.squeryl.annotations.Column
import net.liftweb.record.MetaRecord
import net.liftweb.record.field.StringField
import net.liftweb.record.Record
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.record.field._
import net.liftweb.http.S
import IconhubDb._
import net.liftweb.util.FieldError
import scala.xml.Text
import net.liftweb.common._

class IconSet private() extends MyRecord[IconSet] with KeyedRecord[Long] {
  
  override def meta = IconSet
  override lazy val table = iconSets
  
  @Column(name="id")
  override val idField = new LongField(this)
  
  /*
  def isUnique(f:StringField[IconSet])(s:String):List[FieldError] =
    (from(iconSets)(set => where(f.is === s) compute(count)):Long) {
    case 0 => Nil
    case _ => new FieldError(f, Text(S ? "slug-not-unique")) :: Nil
  }

  val slug = new StringField(this, 128) {
    override def validations = isUnique(this) _ :: super.validations
  }
  */
  
  val slug = new StringField(this, 128) with Unique[IconSet]
  
  val name = new StringField(this, 256)
  val created = new DateTimeField(this)
  
  @Column(name="is_public")
  val isPublic = new BooleanField(this)
  
  @Column(name="owner_id")
  val ownerId = new LongField(this);
  
  lazy val icons = iconSetToIcon.left(this)

  /*
  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[Icon], fetch = FetchType.LAZY)
  var icons : java.util.List[Icon] = new java.util.Vector[Icon]
  */
}

object IconSet extends IconSet with MetaRecord[IconSet] {
  
  def findBySlug(slug: String, principal: Box[Long]): Option[IconSet] = {
    val accessible = principal match {
      case Full(userId) => s:IconSet => s.isPublic === true or s.ownerId === userId
      case _ => s:IconSet => s.isPublic === true
    }
    from(iconSets)(s => where(s.slug === slug and accessible(s)) select(s)).headOption
  }

  def findByUserId(userId: Long): Iterable[IconSet] =
    from(iconSets)(s => where(s.ownerId === userId) select(s))
}