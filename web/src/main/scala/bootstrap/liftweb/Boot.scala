package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.java.sql.{ Connection, DriverManager }
import _root_.net.iconhub.model._
import shiro.Shiro
import shiro.sitemap.Locs._
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.squerylrecord.SquerylRecord
import org.squeryl.Session
import org.squeryl.adapters.PostgreSqlAdapter
import java.util.Locale

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    Class.forName("org.postgresql.Driver")
    SquerylRecord.initWithSquerylSession(Session.create(
      DriverManager.getConnection(Props.get("db.url").get, Props.get("db.user").get, Props.get("db.password").get),
      new PostgreSqlAdapter))
    S.addAround(new LoanWrapper {
      override def apply[T](f: => T): T = {
        inTransaction {
          f
        }
      }
    })
    /*
    transaction {
      IconhubDb.create
    }
    */
    
    LiftRules.localeCalculator = (_) => new Locale("en_US")

    Shiro.init()

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))
    //LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    LiftRules.addToPackages("eu.getintheloop") // Shiro
    LiftRules.addToPackages("net.iconhub")

    LiftRules.setSiteMap(SiteMap(List(
      Menu("Home") / "index" >> Loc.Hidden,
      Menu("Role Test") / "restricted" >> RequireAuthentication >> HasRole("admin"),
      Menu("Login") / "login" >> DefaultLogin >> RequireNoAuthentication,
      Menu("Sign up") / "signup" >> RequireNoAuthentication,
      Menu("My Icons") / "icons" >> RequireAuthentication,
      Menu("About") / "about" >> Hidden >> LocGroup("footer")) ::: Shiro.menus: _*))

    /*
     * Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
     */

    /*
     * Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
     */

    LiftRules.early.append(makeUtf8)

    //LiftRules.loggedInTest = Full(() => User.loggedIn_?)

  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
