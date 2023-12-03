DROP TABLE IF EXISTS OBJECT_KEY;

CREATE TABLE object_key (
   id bigint NOT NULL,
   "key_name" character varying(63) NOT NULL,
   "ui_type" character varying(15) DEFAULT 'text',
   CONSTRAINT "PK_OBJECT_KEY" PRIMARY KEY (id),
   CONSTRAINT "UK_OBJECT_KEY" UNIQUE ("key_name")
);