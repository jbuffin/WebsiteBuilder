# db schema
 
# --- !Ups
CREATE TABLE sites(
	site_id bigint(20) NOT NULL AUTO_INCREMENT,
	hostname varchar(255) NOT NULL,
	site_name varchar(255) NOT NULL,
	PRIMARY KEY (site_id),
	UNIQUE KEY site_name (site_name),
	UNIQUE KEY hostname (hostname)
);

# --- !Downs

DROP TABLE sites;