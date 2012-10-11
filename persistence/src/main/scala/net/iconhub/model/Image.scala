package net.iconhub.model

import javax.persistence._

@Entity
class Image extends IdPk {
  
  @Column(nullable = false)
  var mimeType:String = _
  
  @Column
  var width:Int = _
  
  @Column
  var height:Int = _
  
  @Column
  var content:Array[Byte] = _

  @ManyToOne
  var icon:Icon = _
}

