# public schema
 
# --- !Ups
  
CREATE TABLE widget_types
(
   widget_type_id bigserial NOT NULL,
   type_name varchar(255) NOT NULL,
   CONSTRAINT widget_type_id PRIMARY KEY (widget_type_id)
);

INSERT INTO widget_types (type_name) VALUES ('TEXT');
INSERT INTO widget_types (type_name) VALUES ('CAROUSEL');

CREATE TABLE page_widgets
(
	page_widget_id bigserial NOT NULL,
	page_id bigint NOT NULL,
	widget_id bigint NOT NULL
	CONSTRAINT page_widget_id PRIMARY KEY (page_widget_id)
);

# --- !Downs
DROP TABLE IF EXISTS widget_types;
DROP TABLE IF EXISTS page_widgets;