# public schema
 
# --- !Ups
ALTER TABLE widget
  DROP COLUMN page_order;
ALTER TABLE text_widget
  DROP COLUMN title;
# --- !Downs
ALTER TABLE widget ADD COLUMN page_order int;
ALTER TABLE text_widget ADD COLUMN title varchar(255) NOT NULL DEFAULT '';