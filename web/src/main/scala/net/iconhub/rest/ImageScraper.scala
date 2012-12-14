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
  
  def title(img:Node) = (img \\ "@alt").text
  
    
  def getImages(uri:String): JValue = {
    val baseUrl = new URL(uri)
    
    val html = Http(url(uri) OK as.String)
    val images:Seq[JObject] = Html5.parse(html()) match {
      case Full(xml) => for {
        img <- xml \\ "img"
        src <- img \ "@src"
      } yield {
        JObject(
          List(
            JField("url", JString(new URL(baseUrl, src.text).toString))
          ) ++ (img.attribute("alt") match {
            case Some(x) => List(JField("title", JString(x.text)))
            case _ => Nil
          })
        )
      }
      case _ => Nil
    }
    JArray(images.toList)
  }
  
}