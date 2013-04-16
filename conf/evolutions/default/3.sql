# public schema
 
# --- !Ups
  
CREATE TABLE text_widget
(
   text_widget_id bigserial NOT NULL,
   title varchar(255) NOT NULL,
   text varchar(255) NOT NULL,
   CONSTRAINT text_widget_id PRIMARY KEY (text_widget_id)
);

INSERT INTO text_widget (title, text)
  VALUES ('Test Widget', 'This is a text widget'),
    ('A Text Widget', 'Here you''ll find a lot of text in a text widget. We can put whatever we want here and it will hold it.');

# --- !Downs
DROP TABLE IF EXISTS text_widget;