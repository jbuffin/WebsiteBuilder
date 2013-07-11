# public schema
 
# --- !Ups
ALTER TABLE text_widget
  ADD COLUMN widget_id bigint NOT NULL DEFAULT -1;

# --- !Downs
ALTER TABLE text_widget
  DROP COLUMN widget_id;