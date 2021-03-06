package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import net.liftweb.http.js.jquery.JQuery14Artifacts

/**
 * A class that's instantiated early and run. It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    //Run Mode
    System.setProperty("run.mode", "production")

    // where to search snippet
    LiftRules.addToPackages("com.apistats.lift")

    // Build SiteMap
    val entries = List(Menu.i("Home") / "index",
      Menu("Detail") / "detail",

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
        "Static Content")))

    // set the sitemap. Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries: _*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    //Output HTML5 instead of XHTML
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    //Disable servive as XHTML
    LiftRules.useXhtmlMimeType = false

    //Configure which Javascript framework to use
    LiftRules.jsArtifacts = JQuery14Artifacts

    //Startup MongoDB configuration
    MongoConfig.init

  }
}