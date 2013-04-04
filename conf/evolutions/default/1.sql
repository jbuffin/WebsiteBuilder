# -- churchsite schema
 
# --- !Ups

CREATE SCHEMA churchsite;
  
CREATE TABLE churchsite.sites
(
   site_id bigserial NOT NULL, 
   hostname text NOT NULL, 
   site_name text NOT NULL, 
   site_options bigint NOT NULL, 
   CONSTRAINT site_id PRIMARY KEY (site_id), 
   CONSTRAINT site_name UNIQUE (site_name), 
   CONSTRAINT hostname UNIQUE (hostname)
);


CREATE TABLE churchsite.pages
(
	page_id bigserial NOT NULL,
	uri text NOT NULL,
	title text NOT NULL,
	site_id bigint NOT NULL,
	CONSTRAINT page_id PRIMARY KEY (page_id)
);

# --- !Downs


DROP TABLE churchsite.sites;
DROP TABLE churchsite.pages;
DROP SCHEMA churchsite;