package net.devkat.lift.jpa

import org.scala_libs.jpa.LocalEMF
import net.liftweb.jpa.RequestVarEM

object Model extends LocalEMF("jpaweb") with RequestVarEM {

}