# db schema
 
# --- !Ups
CREATE TABLE sites (
	site_id bigserial PRIMARY KEY,
	hostname varchar(255) NOT NULL,
	site_name varchar(255) NOT NULL,
	site_options bigint(20) NOT NULL,
	UNIQUE KEY site_name,
	UNIQUE KEY hostname
);

CREATE TABLE pages(
	page_id bigserial PRIMARY KEY AUTO_INCREMENT,
	uri varchar(255) NOT NULL,
	title varchar(255) NOT NULL,
	site_id bigint(20) NOT NULL
);

# --- !Downs

DROP TABLE sites;
DROP TABLE pages;
