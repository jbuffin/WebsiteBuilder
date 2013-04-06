package models

case class PageType(typeName: String, pageTypeId: Long = -1)

object PageType {
	
	def getByTypeName(typeName: String): PageType = {
		return PageType(typeName, 1)
	}
	
	def getById(pageTypeId: Long): PageType = {
		return PageType("root", pageTypeId)
	}
	
}