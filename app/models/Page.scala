package models

case class Page(uri: String,
	title: String,
	pageType: Long,/*
	parent: Long,
	children: List[Long],*/
	pageId: Long = -1)

object Page {
	
	

}
