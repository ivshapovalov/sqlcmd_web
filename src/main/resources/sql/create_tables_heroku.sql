
-- Schema: public

-- DROP SCHEMA public;

-- CREATE SCHEMA public
--   AUTHORIZATION postgres ;

GRANT ALL ON SCHEMA public TO uevcoalgyndast;
GRANT ALL ON SCHEMA public TO public;
COMMENT ON SCHEMA public
  IS 'standard public schema';

-- Sequence: user_action_seq

-- DROP SEQUENCE user_action_seq;

CREATE SEQUENCE IF NOT EXISTS user_actions_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 16
  CACHE 1;
ALTER TABLE user_actions_seq
  OWNER TO uevcoalgyndast;

-- Table: user_actions

DROP TABLE IF EXISTS user_actions;

CREATE TABLE user_actions
(
  id integer DEFAULT nextval('user_actions_seq'::regclass),
  database_connection_id integer,
  action text,
  date_time text ,
  CONSTRAINT user_actions_pkey PRIMARY KEY(id)

)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_actions
  OWNER TO uevcoalgyndast;

-- Index: id_pk

DROP INDEX IF EXISTS user_action_id_pk;

CREATE INDEX user_action_id_pk
  ON user_actions
  USING btree
  (id);

CREATE SEQUENCE IF NOT EXISTS database_connections_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER TABLE database_connections_seq
  OWNER TO uevcoalgyndast;


DRop TABLE IF EXISTS database_connections;

CREATE TABLE database_connections
(
  id integer DEFAULT nextval('database_connections_seq'::regclass),
  db_name text,
  user_name text ,
  CONSTRAINT database_connection_pkey PRIMARY KEY(id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE database_connections
  OWNER TO uevcoalgyndast;


ALTER TABLE user_actions
  ADD CONSTRAINT user_action_database_connection_fkey FOREIGN KEY (database_connection_id)
REFERENCES database_connections (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_action_database_connection_fkey
  ON user_actions(database_connection_id);

