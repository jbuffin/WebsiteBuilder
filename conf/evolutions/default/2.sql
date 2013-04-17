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
	widget_id bigint NOT NULL,
	CONSTRAINT page_widget_id PRIMARY KEY (page_widget_id)
);

CREATE TABLE text_widget
(
   text_widget_id bigserial NOT NULL,
   title varchar(255) NOT NULL,
   text varchar(255) NOT NULL,
   CONSTRAINT text_widget_id PRIMARY KEY (text_widget_id)
);

INSERT INTO text_widget (title, text) VALUES ('Test Widget', 'This is a text widget');
INSERT INTO text_widget (title, text) VALUES ('A Text Widget', 'Here you''ll find a lot of text in a text widget. We can put whatever we want here and it will hold it.');
INSERT INTO text_widget (title, text) VALUES ('A Third Widget', 'Here you''ll find a lot of text in a text widget. We can put whatever we want here and it will hold it.');

INSERT INTO page_widgets (page_id, widget_id) VALUES (1, 1);
INSERT INTO page_widgets (page_id, widget_id) VALUES (1, 2);
INSERT INTO page_widgets (page_id, widget_id) VALUES (1, 3);

# --- !Downs
DROP TABLE IF EXISTS widget_types;
DROP TABLE IF EXISTS page_widgets;
DROP TABLE IF EXISTS text_widget;