-- Sequence: database_connection_seq

-- DROP SEQUENCE database_connection_seq;

CREATE SEQUENCE database_connection_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE database_connection_seq
  OWNER TO postgres;


-- Table: database_connection

-- DROP TABLE database_connection;

CREATE TABLE database_connection
(
  id integer DEFAULT nextval('database_connection_seq'::regclass),
  db_name text,
  user_name text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE database_connection
  OWNER TO postgres;

DROP INDEX database_connection_id_pk;

DROP INDEX id_pk;

-- Constraint: user_actions_pkey

-- ALTER TABLE user_actions DROP CONSTRAINT user_actions_pkey;

ALTER TABLE user_actions
  ADD CONSTRAINT user_actions_pkey PRIMARY KEY(id);

-- Constraint: database_connection_pkey

-- ALTER TABLE database_connection DROP CONSTRAINT database_connection_pkey;

ALTER TABLE database_connection
  ADD CONSTRAINT database_connection_pkey PRIMARY KEY(id);

-- Column: database_connection_id

-- ALTER TABLE user_actions DROP COLUMN database_connection_id;

ALTER TABLE user_actions ADD COLUMN database_connection_id integer;

ALTER TABLE user_actions
  ADD CONSTRAINT user_action_database_connection_fkey FOREIGN KEY (database_connection_id) REFERENCES database_connection (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_action_database_connection_fkey
  ON user_actions(database_connection_id);


-- Data migration

INSERT INTO database_connection(id, user_name, db_name)
SELECT nextval('database_connection_seq'::regclass) AS id, subquery.user_name AS user_name, subquery.db_name AS db_name
FROM (SELECT DISTINCT user_name, db_name FROM user_actions) subquery

UPDATE user_actions
SET database_connection_id=subquery.id
FROM (SELECT id FROM database_connection) AS subquery
WHERE user_actions.user_name=subquery.user_name AND user_actions.db_name=subquery.db_name;

-- drop unused columns

ALTER TABLE user_actions DROP COLUMN user_name;
ALTER TABLE user_actions DROP COLUMN db_name;