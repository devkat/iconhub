package net.iconhub.model

import javax.persistence._

@Entity
class IconSet extends IdPk {
  
  @Column
  var slug:String = _
  
  @Column
  var name:String = _
  
  @ManyToOne(optional = false)
  var owner : User = _
  
  @Column
  var isPublic:Boolean = _

  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[Icon], fetch = FetchType.LAZY)
  var icons : java.util.List[Icon] = new java.util.Vector[Icon]
}

