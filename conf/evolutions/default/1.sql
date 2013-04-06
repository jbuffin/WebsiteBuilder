# public schema
 
# --- !Ups
  
CREATE TABLE sites
(
   site_id bigserial NOT NULL,
   hostname varchar(255) NOT NULL,
   site_name varchar(255) NOT NULL,
   CONSTRAINT site_id PRIMARY KEY (site_id), 
   CONSTRAINT site_name UNIQUE (site_name), 
   CONSTRAINT hostname UNIQUE (hostname)
);

CREATE TABLE pages
(
	page_id bigserial NOT NULL,
	uri varchar(32) NOT NULL,
	title varchar(255) NOT NULL,
	page_type bigint NOT NULL,
	parent bigint,
	site_id bigint NOT NULL,
	CONSTRAINT page_id PRIMARY KEY (page_id)
);

CREATE TABLE page_types
(
	type_id bigserial NOT NULL,
	type_name varchar(255),
	CONSTRAINT type_id PRIMARY KEY (type_id)
);

INSERT INTO page_types
	(type_name)
	VALUES ('ROOT');
INSERT INTO page_types
	(type_name)
	VALUES ('PAGE');
INSERT INTO page_types
	(type_name)
	VALUES ('BLOG_ROOT');
INSERT INTO page_types
	(type_name)
	VALUES ('BLOG_POST');
INSERT INTO page_types
	(type_name)
	VALUES ('SUBROOT');

# --- !Downs
DROP TABLE IF EXISTS sites;
DROP TABLE IF EXISTS pages;
DROP TABLE IF EXISTS page_types;