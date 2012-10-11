package net.iconhub.model

import javax.persistence._

trait IdPk {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id:Long = _
  
}