# public schema
 
# --- !Ups
CREATE TABLE widget
(
  widget_id bigserial NOT NULL, 
  widget_type bigint NOT NULL,
   CONSTRAINT widget_id PRIMARY KEY (widget_id)
);

# --- !Downs
DROP TABLE widget;