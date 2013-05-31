# public schema
 
# --- !Ups
ALTER TABLE widget
  ADD CONSTRAINT widget_id PRIMARY KEY (widget_id);
  
# --- !Downs
ALTER TABLE widget
  DROP CONSTRAINT widget_id;