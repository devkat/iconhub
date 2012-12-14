package net.iconhub.rest

import net.liftweb.http._
import net.liftweb.http.rest.RestHelper
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.json._
import dispatch._
import scala.xml._
import java.net.URL

object ImageScraper extends RestHelper {
  
  serve( "api" / "imagescraper" prefix {
    
    case Nil JsonGet _ => for {
        uri <- S.param("uri") ?~ "Request parameter 'uri' missing"
      } yield getImages(uri)
    
  })
  
  def getImages(uri:String): JValue = {
    val baseUrl = new URL(uri)
    
    val html = Http(url(uri) OK as.String)
    val sources:Seq[String] = Html5.parse(html()) match {
      case Full(xml) => (xml \\ "img" \\ "@src") map (_.text)
      case _ => Nil
    }
    JArray(sources.toList
        map (url => new URL(baseUrl, url).toString)
        map (src => JString(src)))
  }
  
}