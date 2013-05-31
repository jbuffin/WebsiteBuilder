# public schema
 
# --- !Ups
DROP TABLE widget;

# --- !Downs
CREATE TABLE widget
(
  widget_id bigserial, 
  widget_type bigint NOT NULL
);
ALTER TABLE widget
  ADD CONSTRAINT widget_id PRIMARY KEY (widget_id);