package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.libs.json
import play.api.libs.json._
import play.api.libs.functional.syntax._

import models._

case class Site(
	siteName: String,
	hostName: String,
	siteId: Long = -1)

object Site {
	
	implicit val siteReads = (
		(__ \ "siteName").read[String] ~
		(__ \ "hostName").read[String] ~
		(__ \ "id").read[Long]
	) (Site.apply _)

	val simple = {
		get[String]("sites.site_name") ~
			get[String]("sites.hostname") ~
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
					select * from sites
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