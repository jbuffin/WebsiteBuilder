# public schema
 
# --- !Ups
drop table pages;
drop table page_types;
drop table page_rows;
drop table text_widget;
drop table widget;
drop table widget_types;
# --- !Downs
CREATE TABLE pages
(
  page_id bigserial NOT NULL,
  uri character varying(32) NOT NULL,
  title character varying(255) NOT NULL,
  page_type bigint NOT NULL,
  parent bigint,
  site_id bigint NOT NULL,
  CONSTRAINT page_id PRIMARY KEY (page_id)
);

CREATE TABLE page_types
(
  type_id bigserial NOT NULL,
  type_name character varying(255),
  CONSTRAINT type_id PRIMARY KEY (type_id)
);

CREATE TABLE page_rows
(
  page_id bigint NOT NULL,
  page_row_id bigserial NOT NULL,
  row_order integer,
  CONSTRAINT page_row_id PRIMARY KEY (page_row_id),
  CONSTRAINT unique_row_order UNIQUE (page_id, row_order)
);

CREATE TABLE text_widget
(
  text_widget_id bigserial NOT NULL,
  text text NOT NULL,
  widget_id bigint NOT NULL DEFAULT (-1),
  CONSTRAINT text_widget_id PRIMARY KEY (text_widget_id)
);

CREATE TABLE widget
(
  widget_id bigserial NOT NULL,
  widget_type bigint NOT NULL,
  page_id integer,
  page_row integer,
  row_order integer,
  CONSTRAINT widget_id PRIMARY KEY (widget_id)
);

CREATE TABLE widget_types
(
  widget_type_id bigserial NOT NULL,
  type_name character varying(255) NOT NULL,
  CONSTRAINT widget_type_id PRIMARY KEY (widget_type_id)
);