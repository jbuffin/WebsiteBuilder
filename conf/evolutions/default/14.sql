# public schema
 
# --- !Ups
CREATE TABLE page_rows
(
   page_id bigint NOT NULL, 
   page_row_id bigserial NOT NULL, 
   row_order integer, 
   CONSTRAINT page_row_id PRIMARY KEY (page_row_id), 
   CONSTRAINT unique_row_order UNIQUE (page_id, row_order)
);

ALTER TABLE widget
  ADD COLUMN page_row integer;
ALTER TABLE widget
  ADD COLUMN row_order integer;

# --- !Downs
DROP TABLE page_rows;

ALTER TABLE widget
  DROP COLUMN page_row;
ALTER TABLE widget
  DROP COLUMN row_order;