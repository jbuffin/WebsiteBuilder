package controllers

import play.api._
import play.api.mvc._
import models._
import models.widgets._
import play.api.templates.Html

object Sites extends Controller {

	def index = Action { implicit request =>
		Logger.debug("[Sites.index]: request.domain: '"+request.domain+"'")
		try {
			val site = Site.getSiteByHostName(request.domain).get
			val page = Page.getPageByUri(site.siteId).get
			val listOfWidgets: List[Long] = Widgets.getWidgetList(page.pageId)
			if (listOfWidgets.length != 3) {
				throw new NoSuchElementException
			}
			Ok(views.html.sites.templates.gracechurch.index(page.title, listOfWidgets))
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}

	def getPageFromUri(page: String) = Action { implicit request =>
		val site = Site.getSiteByHostName(request.domain).get
		
		//Ok(views.html.sites.index(Widgets.getWidgetList(Site.getSiteByHostName(request.domain).get.siteId)))
		Redirect(routes.Sites.index)
	}
	
	def createNewSite(site: Site, page: Page, textWidgets: List[Text]) = {
		Site.create(site)
		Page.create(page)
		textWidgets.foreach(Text.create)
	}

}
