package net.iconhub.model

import org.squeryl.annotations.Column
import java.sql.Timestamp

class IconSet(
    val slug: String,
    val name: String,
    val created: Timestamp,
    @Column("owner_id")
    val ownerId: Long,
    @Column("is_public")
    val isPublic: Boolean
) {
  
  /*
  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[Icon], fetch = FetchType.LAZY)
  var icons : java.util.List[Icon] = new java.util.Vector[Icon]
  */
}

