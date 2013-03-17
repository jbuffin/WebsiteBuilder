package models

import play.api._
import play.api.db._
import play.api.Play.current
import play.api.libs._
import anorm._
import anorm.SqlParser._

case class Site(
	siteName: String,
	hostName: String,
	siteId: Long = -1
)

object Site {
	
	val simple = {
		get[String] ("sites.site_name") ~
		get[String] ("sites.hostname") ~
		get[Long]("sites.site_id") map {
			case site_name ~ hostname ~ site_id => Site(site_name, hostname, site_id)
		}
	}
	
	def getAll: Seq[Site] = {
		DB.withConnection { implicit connection =>
			SQL("select * from sites").as(Site.simple *)
		}
	}
	
	def getSiteByHostName(hostName: String): Option[Site] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select site_name, hostname, site_id from sites
						where hostname = {hostname}
				"""
			).on(
				'hostname -> hostName
			).as(Site.simple.singleOpt)
		}
	}
	
	def getSiteById(siteId: Long): Option[Site] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from sites
						where site_id = {site_id}
				"""
			).on(
				'site_id -> siteId
			).as(Site.simple.singleOpt)
		}
	}
	
	def create(site: Site): Long = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into sites (site_name, hostname) values (
					{site_name}, {hostname}
				)
				""").on(
					'site_name -> site.siteName,
					'hostname -> site.hostName).executeInsert()
		} match {
			case Some(long) => long
			case None => -1
		}
	}
	
}