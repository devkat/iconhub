package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import _root_.java.sql.{Connection, DriverManager}
import _root_.net.iconhub.model._
import net.iconhub.openid.DefaultOpenIDVendor
import net.iconhub.model.User
import shiro.Shiro;
import shiro.sitemap.Locs._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url").get,
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }
    
    Shiro.init()

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))
    //LiftRules.loggedInTest = Full(() => User.loggedIn_?)
    LiftRules.dispatch.append(DefaultOpenIDVendor.dispatchPF)
    LiftRules.snippets.append(DefaultOpenIDVendor.snippetPF)

    LiftRules.addToPackages("eu.getintheloop") // Shiro
    LiftRules.addToPackages("net.iconhub")

    // Build SiteMap
    LiftRules.setSiteMap(SiteMap(
      //Menu(Loc("Home", "index" :: Nil, "Home", Hidden)),
      Menu("Home") / "index" >> Loc.Hidden,
      Menu(Loc("", new Link(Nil, false), "My Account", Loc.PlaceHolder), 
          Menu("Login") / "user_mgt" / "login"
      ),//, User.AddUserMenusUnder)),
      
      Menu("My Icons") / "icons" >> RequireAuthentication,
      
      //Menu(Loc.PlaceHolder"My Account") / "" >> User.AddUserMenusUnder,
      // Menu with special Link
      //Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content")),
      Menu("About") / "about" >> Hidden >> LocGroup("footer")
    ))
    
    LiftRules.setSiteMap(SiteMap(List(
      Menu("Home") / "index" >> RequireAuthentication,
      Menu("Role Test") / "restricted" >> RequireAuthentication >> HasRole("admin"),
      Menu("Login") / "login" >> DefaultLogin >> RequireNoAuthentication
      ) ::: Shiro.menus: _*
    ))

    //LiftRules.setSiteMapFunc(() => User.sitemapMutator(sitemap()))

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

    S.addAround(DB.buildLoanWrapper)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
