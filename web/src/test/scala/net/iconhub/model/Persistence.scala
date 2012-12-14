package net.iconhub.model

import org.scalatest._
import java.sql.DriverManager
import net.liftweb.squerylrecord.SquerylRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import java.sql.SQLNonTransientConnectionException
import org.squeryl.Session
import org.squeryl.adapters.DerbyAdapter
import java.sql.SQLException

trait Persistence extends FlatSpec {
  
  val dbConnection = "jdbc:derby:memory:iconhub"

  def withDb(testCode: () => Any) {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
    val connection = DriverManager.getConnection(dbConnection + ";create=true")
    
    SquerylRecord.initWithSquerylSession(Session.create(connection, new DerbyAdapter))

    //SessionFactory.concreteFactory = Some(() => Session.create(connection, new DerbyAdapter))
    try {
      transaction {
        IconhubDb.create
        testCode()
        //finally IconhubDb.drop
      }
    }
    finally {
      connection.close
      //intercept[SQLNonTransientConnectionException] {
      intercept[SQLException] {
        // http://db.apache.org/derby/docs/dev/devguide/tdevdvlp40464.html
        DriverManager.getConnection(dbConnection + ";shutdown=true");
      }
    }
  }



}