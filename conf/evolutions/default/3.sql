# public schema
 
# --- !Ups
  
CREATE TABLE text_widget
(
   text_widget_id bigserial NOT NULL,
   title varchar(255) NOT NULL,
   text varchar(255) NOT NULL,
   CONSTRAINT text_widget_id PRIMARY KEY (text_widget_id)
);

# --- !Downs
DROP TABLE IF EXISTS text_widget;
