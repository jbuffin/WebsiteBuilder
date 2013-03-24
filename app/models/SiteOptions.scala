package models



case class SiteOptions(siteOptionsId: Long)

object SiteOptions {
	
	def getById(siteOptionsId: Long): Option[SiteOptions] = {
		Option(SiteOptions(1))
	}
	
}