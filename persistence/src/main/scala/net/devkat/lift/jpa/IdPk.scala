package net.devkat.lift.jpa

import javax.persistence._
import javax.persistence.GeneratedValue
import javax.persistence.Id

trait IdPk {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id:Long = _
  
}