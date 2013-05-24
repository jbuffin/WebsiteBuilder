package controllers

import play.api._
import play.api.mvc._
import models._
import models.widgets._
import play.api.templates.Html

object Sites extends Controller {

	def getPageFromUri(uri: String = "") = Action { implicit request =>
		Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
		try {
			val site = Site.getSiteByHostName(request.domain).get
			val page = Page.getPageByUri(site.siteId, uri).get
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

	def getChildPage(page: String) = Action { implicit request =>
		val parsedPages = page.split("/").toList
		try {
			val site = Site.getSiteByHostName(request.domain).get

			Ok(parsedPages.last)
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}

	def createNewSite(site: Site, page: Page, textWidgets: List[Text]) = {
		Site.create(site)
		Page.create(page)
		textWidgets.foreach(Text.create)
	}

}
