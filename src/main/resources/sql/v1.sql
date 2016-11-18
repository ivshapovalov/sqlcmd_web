-- Database: sqlcmd_log

-- DROP DATABASE sqlcmd_log;

CREATE DATABASE sqlcmd_log
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Ukrainian_Ukraine.1251'
       LC_CTYPE = 'Ukrainian_Ukraine.1251'
       CONNECTION LIMIT = -1;

-- Schema: public

-- DROP SCHEMA public;

CREATE SCHEMA public
  AUTHORIZATION postgres;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
COMMENT ON SCHEMA public
  IS 'standard public schema';

-- Sequence: user_action_seq

-- DROP SEQUENCE user_action_seq;

CREATE SEQUENCE user_action_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 16
  CACHE 1;
ALTER TABLE user_action_seq
  OWNER TO postgres;

-- Table: user_actions

-- DROP TABLE user_actions;

CREATE TABLE user_actions
(
  id integer DEFAULT nextval('user_action_seq'::regclass),
  user_name text,
  db_name text,
  action text,
  date_time text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_actions
  OWNER TO postgres;

-- Index: id_pk

-- DROP INDEX id_pk;

CREATE INDEX id_pk
  ON user_actions
  USING btree
  (id);

