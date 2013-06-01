# public schema
 
# --- !Ups

CREATE TABLE carousel_images
(
   carousel_images_id bigserial NOT NULL,
   image varchar(255) NOT NULL,
   title varchar(255) NOT NULL,
   text varchar(255) NOT NULL,
   widget_id bigint NOT NULL,
   CONSTRAINT carousel_images_id PRIMARY KEY (carousel_images_id)
);

# --- !Downs
DROP TABLE carousel_images;