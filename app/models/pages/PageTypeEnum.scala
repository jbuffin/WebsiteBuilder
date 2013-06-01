package models.pages

object PageTypeEnum extends Enumeration {
	type PageTypeEnum = Value
	
	val ROOT = Value("ROOT")
	val PAGE = Value("PAGE")
	val BLOG_ROOT = Value("BLOG_ROOT")
	val BLOG_POST = Value("BLOG_POST")
	val SUBROOT = Value("SUBROOT")

}