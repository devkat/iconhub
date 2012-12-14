package net.iconhub.sitemap

import net.liftweb.sitemap.Loc
import net.liftweb.common._
import net.iconhub.model.IconSet
import net.liftweb.http.RewriteRequest
import net.liftweb.http.ParsePath
import net.iconhub.model.User
import net.liftweb.http.RewriteResponse
import scala.xml._
import net.iconhub.snippet.IconSets
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.util.Helpers._

abstract class IconSetInfo
case object NoSuchIconSet extends IconSetInfo
case object NotPublic extends IconSetInfo
case class FullIconSetInfo(set : IconSet) extends IconSetInfo

object IconSetLoc extends Loc[IconSetInfo] {
  
  override def rewrite = Full(inTransaction {
    case RewriteRequest(ParsePath(List("set", slug), "", true, false), _, _) => inTransaction {
      val setInfo = IconSet.findBySlug(slug, User.currentUserId) match {
        case Some(iconSet) => FullIconSetInfo(iconSet)
        case _ => NoSuchIconSet
      }
      (RewriteResponse("set" :: Nil), setInfo)
    }
    /*
    case RewriteRequest(ParsePath(List("set", IconSet(iconSet)), "", true, false), _, _) => {
         if (iconSet.isPublic.is || iconSet.ownerId == User.currentUserId)
             (RewriteResponse("set" :: Nil), FullIconSetInfo(iconSet))
           else
             (RewriteResponse("set" :: Nil), NoSuchIconSet)
    }
       */
  })
  
  override def snippets = {
  case ("show", Full(NoSuchIconSet)) => {ignore: NodeSeq =>
    Text("Icon set not found.")}
  case ("show", Full(NotPublic)) => {ignore : NodeSeq =>
    Text("This icon set is not publicly viewable")}
  case ("show", Full(FullIconSetInfo(set))) =>
    IconSets.show(set) _
  case ("my", Full(FullIconSetInfo(set))) => {n: NodeSeq =>
    User.currentUserId match {
      case Full(userId) if userId == set.ownerId.get => n
      case _ => Nil
    }
  }
    /*
  case ("my", Full(FullIconSetInfo(set))) => {n: NodeSeq =>
      User.currentUserId match {
        case Full(set.ownerId) => n
        case _ => Nil
      }
    }
    */
  case ("my", _) => n: NodeSeq => Nil
  }
  
  override def params = Nil
  
  override def defaultValue = Empty
  
  override def text = new Loc.LinkText[IconSetInfo](_ => Nil)
  
  override def link = new Loc.Link[IconSetInfo](List("set"), false) {
    override def createLink(info: IconSetInfo) =
      info match {
      case FullIconSetInfo(set) => Full(Text(calcHref(info)))
      case _ => Empty
    }
  }
  
  override def calcHref(info:IconSetInfo) =
    info match {
      case FullIconSetInfo(set) => "/set/" + urlEncode(set.slug.is)
    }
  
  override def name = "set"

}

