# db schema
 
# --- !Ups
CREATE TABLE sites(
	site_id bigint(20) NOT NULL AUTO_INCREMENT,
	hostname varchar(255) NOT NULL,
	site_name varchar(255) NOT NULL,
	site_options bigint(20) NOT NULL,
	PRIMARY KEY (site_id),
	UNIQUE KEY site_name (site_name),
	UNIQUE KEY hostname (hostname)
);

CREATE TABLE pages(
	page_id bigint(20) NOT NULL AUTO_INCREMENT,
	uri varchar(255) NOT NULL,
	title varchar(255) NOT NULL,
	site_id bigint(20) NOT NULL,
	PRIMARY KEY (page_id)
);

# --- !Downs

DROP TABLE sites;