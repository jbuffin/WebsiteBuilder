# public schema
 
# --- !Ups
CREATE TABLE widget
(
  widget_id bigserial, 
  widget_type bigint NOT NULL
);

# --- !Downs
DROP TABLE widget;