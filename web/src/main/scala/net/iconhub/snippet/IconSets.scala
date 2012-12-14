package net.iconhub.snippet

import net.iconhub.model.IconSet
import scala.xml.NodeSeq
import net.iconhub.model.User
import net.liftweb.common.Full
import net.iconhub.model.Icon

object IconSets {
  
  def show(set:IconSet)(n:NodeSeq): NodeSeq = {
    <h2>{set.name}</h2> ++ (set.icons.toList match {
      case Nil => <p>This set is empty.</p>
      case icons =>
        <ul>
          {icons.map(icon => <li>{icon.name}</li>)}
        </ul>
    })
  }
  
  def listMyIconSets(n:NodeSeq): NodeSeq =
    User.currentUserId match {
      case Full(userId) =>
        <ul>
          {IconSet.findByUserId(userId).map(set =>
            <li><a href={"/set/" + set.slug}>{set.name}</a></li>)}
        </ul>
      case _ => Nil
    }
  
}