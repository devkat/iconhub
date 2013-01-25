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
import net.iconhub.auth.IconhubOpenIdVendor
import net.iconhub.sitemap.IconSetLoc
import net.iconhub.rest.ImageScraper
import net.liftweb.sitemap.Menu.Menuable
import net.liftweb.sitemap.Loc.MenuCssClass

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
    
    LiftRules.resourceNames = "iconhub" :: Nil
    LiftRules.localeCalculator = (_) => Locale.ENGLISH
    LiftRules.dispatch.append(IconhubOpenIdVendor.dispatchPF)
    LiftRules.snippets.append(IconhubOpenIdVendor.snippetPF)
    LiftRules.statelessDispatch.append(ImageScraper)
    
    val staticFiles: PartialFunction[Req, Boolean] = {
      case Req("static" :: _, _, _) => false
    }
    LiftRules.liftRequest.append(staticFiles)

    Shiro.init()

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))
    //LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    //LiftRules.addToPackages("eu.getintheloop") // Shiro
    LiftRules.addToPackages("net.iconhub")
    LiftRules.addToPackages("shiro")
    
    val items = List[ConvertableToMenu](
      Menu("home", "Home") / "index" >> DefaultLogin >> Loc.Hidden,
      Menu("login", "Login") / "login" >> DefaultLogin >> RequireNoAuthentication,
      Menu("my_account", "My account") / "my" / "account" >> PlaceHolder >> RequireAuthentication submenus(
        (Menu("My profile") / "profile" >> RequireAuthentication) :: Shiro.menus
      ),
      //Menu("Role Test") / "restricted" >> RequireAuthentication >> HasRole("admin"),
      Menu("Sign up") / "signup" >> RequireNoAuthentication >> Loc.Hidden,
      Menu("explore", "Explore") / "explore",
      Menu("my_iconhub", "My iconhub") / "my" >> PlaceHolder >> RequireAuthentication submenus(
        Menu("My icons") / "my" / "icons" >> RequireAuthentication submenus (
          Menu("Create") / "my" / "icons" / "create" >> RequireAuthentication >> Loc.Hidden
        ),
        Menu("My sets") / "my" / "sets" >> RequireAuthentication submenus(
          Menu("Create") / "my" / "sets" / "create" >> RequireAuthentication >> Loc.Hidden
        ),
        Menu("Import from web") / "my" / "import" >> RequireAuthentication
      ),
      Menu(IconSetLoc), // >> RequireAuthentication
      Menu("About") / "about" >> Hidden >> LocGroup("footer"),
      Menu("Static") / "static" / ** >> Hidden
    )
    
    def addCssClass(c:ConvertableToMenu) = c match {
      case m:Menuable => m >> MenuCssClass("menu-item-" + m.name)
      case _ => c
    }

    LiftRules.setSiteMap(SiteMap(items map addCssClass _ : _*))

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
