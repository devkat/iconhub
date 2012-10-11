/*
 * Copyright 2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package net.iconhub.model

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

import javax.persistence._

class JpaTest {
  var emf : EntityManagerFactory = _

  @Before
  def initEMF () = {
    emf = Persistence.createEntityManagerFactory("jpaweb")
    try {
    } catch {
      case e: Exception => {
        def printAndDescend(ex : Throwable) : Unit = {
          println(e.getMessage())
          if (ex.getCause() != null) {
            printAndDescend(ex.getCause())
          }
        }
        printAndDescend(e)
      }
    }
  }

  @After
  def closeEMF () = {
    if (emf != null) emf.close()
  }

  @Test
  def save_stuff () = {
    var em = emf.createEntityManager()

    val tx = em.getTransaction()

    tx.begin()

    val user = new User
    user.email = "andreas@devkat.net"

    em.persist(user)

    val icon = new Icon
    icon.name = "Huh?"
    icon.created = new _root_.java.util.Date
    icon.owner = user
    em.persist(icon)

    tx.commit()
    em.close()

    // Re-open and query
    em = emf.createEntityManager()

    val retrieved = em.createNamedQuery("Icon.findAll").getResultList().asInstanceOf[java.util.List[Icon]]

    assertEquals(1, retrieved.size())
    assertSame(user.id, retrieved.get(0).owner.id)

    println("Found icon '" + retrieved.get(0).name + "'")

    // clean up
    em.getTransaction().begin()
    em.remove(em.getReference(classOf[Icon], icon.id))
    em.remove(em.getReference(classOf[User], user.id))
    em.getTransaction().commit()

    em.close()
  }
}
