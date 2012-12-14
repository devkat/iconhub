package net.iconhub.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.dsl.CompositeKey2
import net.liftweb.squerylrecord.RecordTypeMode._

class IconSetToIcon(
    @Column("iconset_id")
    val iconSetId: Long,
    @Column("icon_id")
    val iconId: Int)
  extends KeyedEntity[CompositeKey2[Long, Long]] {
  def id = compositeKey(iconSetId, iconId)
}
