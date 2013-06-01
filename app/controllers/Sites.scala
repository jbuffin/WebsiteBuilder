package controllers

import play.api._
import play.api.mvc._
import models._
import models.widgets._
import play.api.templates.Html
import models.pages.Page
import models.pages.PageType
import models.pages.PageTypeEnum
import models.pages.PageTypeEnum._

object Sites extends Controller {

	def getPageFromUri(uri: String = "") = Action { implicit request =>
		Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
		try {
			val site = Site.getSiteByHostName(request.domain).get
			val page = Page.getPageByUri(site.siteId, uri).get
			pageTypeChooser(PageType.getById(page.pageType).get, page)
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}
	
	def pageTypeChooser(pageType: PageType, page: Page) = {
		try {
			val listOfWidgets: List[Long] = Widgets.getWidgetList(page.pageId)
			if (listOfWidgets.length != 4) {
				Logger.error("Not enough widgets: "+listOfWidgets.length)
				throw new NoSuchElementException
			}
			PageTypeEnum.withName(pageType.typeName) match {
				case ROOT => Ok(views.html.sites.templates.gracechurch.index(page.title, listOfWidgets))
				case _ => NotFound
			}
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}

}
