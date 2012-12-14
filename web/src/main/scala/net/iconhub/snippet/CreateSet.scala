package net.iconhub.snippet

import net.liftweb.http.S
import net.iconhub.http.{BootstrapScreen => Screen}
import net.iconhub.model._
import net.iconhub.sitemap.IconSetLoc
import net.iconhub.sitemap.FullIconSetInfo


object CreateSet extends Screen {
  
  override def screenTop = <b>Create new icon set</b>
  
  object set extends ScreenVar(IconSet.createRecord)
  
  addFields(() => set.is.name)
  addFields(() => set.is.slug)

  def finish() {
    set.ownerId(User.currentUserId)
    IconhubDb.iconSets.insert(set.is)
    S.redirectTo(IconSetLoc.calcHref(FullIconSetInfo(set.is)))
  }

}