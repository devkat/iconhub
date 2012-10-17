package net.iconhub.model

import java.sql.Timestamp
import org.squeryl.annotations.Column

class Icon (
    val id: Long,
    val slug: String,
    val name: String,
    val created: Timestamp,
    @Column("owner_id")
    val ownerId: Long
    ) {

  /*
  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[IconSet], fetch = FetchType.LAZY)
  var sets:java.util.List[IconSet] = new java.util.Vector[IconSet]

  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[Image], fetch = FetchType.LAZY)
  var images:java.util.List[Image] = new java.util.Vector[Image]
  */

}
