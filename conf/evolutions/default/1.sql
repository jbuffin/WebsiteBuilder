# public schema
 
# --- !Ups
  
CREATE TABLE sites
(
   site_id bigserial NOT NULL, 
   hostname text NOT NULL, 
   site_name text NOT NULL, 
   site_options bigint NOT NULL, 
   CONSTRAINT site_id PRIMARY KEY (site_id), 
   CONSTRAINT site_name UNIQUE (site_name), 
   CONSTRAINT hostname UNIQUE (hostname)
);


CREATE TABLE pages
(
	page_id bigserial NOT NULL,
	uri text NOT NULL,
	title text NOT NULL,
	site_id bigint NOT NULL,
	CONSTRAINT page_id PRIMARY KEY (page_id)
);

# --- !Downs
DROP TABLE sites;
DROP TABLE pages;