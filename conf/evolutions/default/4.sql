# public schema
 
# --- !Ups
ALTER TABLE page_widgets
  ADD COLUMN widget_type bigint NOT NULL DEFAULT -1;

# --- !Downs
ALTER TABLE page_widgets
  DROP COLUMN widget_type;