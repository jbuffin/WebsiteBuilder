# public schema
 
# --- !Ups
ALTER TABLE text_widget
   ALTER COLUMN text TYPE text;

# --- !Downs

ALTER TABLE text_widget
  ALTER COLUMN text TYPE varchar(255);