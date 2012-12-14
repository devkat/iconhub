package net.iconhub.snippet

import net.liftweb.http.S
import net.iconhub.http.{BootstrapScreen => Screen}
import net.iconhub.model._
import net.iconhub.sitemap.IconSetLoc
import net.iconhub.sitemap.FullIconSetInfo


object CreateIcon extends Screen {
  
  override def screenTop = <b>Create new icon</b>
  
  object icon extends ScreenVar(Icon.createRecord)
  
  addFields(() => icon.is.name)
  addFields(() => icon.is.slug)

  def finish() {
    icon.ownerId(User.currentUserId)
    IconhubDb.icons.insert(icon.is)
    //S.redirectTo(IconSetLoc.calcHref(FullIconSetInfo(set.is)))
  }

}