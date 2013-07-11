# public schema
 
# --- !Ups
ALTER TABLE widget ADD COLUMN page_id int;
DROP TABLE page_widgets;

# --- !Downs
ALTER TABLE widget DROP COLUMN page_id;
CREATE TABLE page_widgets
(
	page_widget_id bigserial NOT NULL,
	page_id bigint NOT NULL,
	widget_id bigint NOT NULL,
	CONSTRAINT page_widget_id PRIMARY KEY (page_widget_id)
);
INSERT INTO page_widgets (page_id, widget_id) VALUES (1, 1);
INSERT INTO page_widgets (page_id, widget_id) VALUES (1, 2);
INSERT INTO page_widgets (page_id, widget_id) VALUES (1, 3);
