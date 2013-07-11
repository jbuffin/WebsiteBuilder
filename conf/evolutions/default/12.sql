# public schema
 
# --- !Ups
ALTER TABLE widget ADD COLUMN page_order int;

# --- !Downs
ALTER TABLE widget DROP COLUMN page_order;