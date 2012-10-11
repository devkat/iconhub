package net.iconhub.model

import java.util.Date
import javax.persistence._

@Entity
@NamedQuery(name="Icon.findAll", query="SELECT i FROM Icon i")
class Icon extends IdPk {

  @Column
  var slug:String = _
  
  @Column
  var name:String = _
  
  @Temporal(TemporalType.DATE)
  @Column(nullable = true)
  var created:Date = new Date()
  
  @ManyToOne(optional = false)
  var owner:User = _

  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[IconSet], fetch = FetchType.LAZY)
  var sets:java.util.List[IconSet] = new java.util.Vector[IconSet]

  @ManyToMany(cascade = Array(CascadeType.ALL), targetEntity =  classOf[Image], fetch = FetchType.LAZY)
  var images:java.util.List[Image] = new java.util.Vector[Image]

}
