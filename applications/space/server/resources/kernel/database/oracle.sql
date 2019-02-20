PROMPT Use 'GRANT EXECUTE ON CTX_DDL TO [username]';

PROMPT Creating FullText Lexer Preference monet_lexer ...
begin
  ctx_ddl.create_preference('monet_lexer', 'BASIC_LEXER');
  ctx_ddl.set_attribute('monet_lexer', 'base_letter', 'YES');
  ctx_ddl.create_preference('wildcard_pref', 'BASIC_WORDLIST');
  ctx_ddl.set_attribute('wildcard_pref', 'WILDCARD_MAXTERMS', 50000);  
end;

/

PROMPT Creating Table ts$info ...
CREATE TABLE ts$info (
  name VARCHAR2 (255) NOT NULL,
  value VARCHAR2 (255) NOT NULL
);
INSERT INTO ts$info (name, value) VALUES ('version', '3.3.1');

PROMPT Creating Sequence ts$nodes_ID_SEQ ...
CREATE SEQUENCE  ts$nodes_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$services_ID_SEQ ...
CREATE SEQUENCE  ts$services_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$tasks_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$tasks_orders_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_orders_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$tasks_orders_chats_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_orders_chats_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$cubes_ID_SEQ ...
CREATE SEQUENCE  ts$cubes_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$sources_ID_SEQ ...
CREATE SEQUENCE  ts$sources_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$nodes_permissions_ID_SEQ ...
CREATE SEQUENCE  ts$nodes_permissions_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$nodes_revisions_ID_SEQ ...
CREATE SEQUENCE  ts$nodes_revisions_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$nodes_superdata_ID_SEQ ...
CREATE SEQUENCE  ts$nodes_superdata_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$reports_ID_SEQ ...
CREATE SEQUENCE  ts$reports_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$tasks_facts_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_facts_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
PROMPT Creating Sequence ts$tasks_snapshots_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_snapshots_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$logbooknode_ID_SEQ ...
CREATE SEQUENCE  ts$logbooknode_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$cubes_reports_ID_SEQ ...
CREATE SEQUENCE  ts$cubes_reports_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$cubes_filtergroups_ID_SEQ ...
CREATE SEQUENCE  ts$cubes_filtergroups_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$tasks_timers_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_timers_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$eventlog_ID_SEQ ...
CREATE SEQUENCE  ts$eventlog_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$notifications_ID_SEQ ...
CREATE SEQUENCE  ts$notifications_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
PROMPT Creating Sequence ts$workqueue_ID_SEQ ...
CREATE SEQUENCE  ts$workqueue_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$locations_ID_SEQ ...
CREATE SEQUENCE  ts$locations_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$posts_ID_SEQ ...
CREATE SEQUENCE  ts$posts_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$post_comments_ID_SEQ ...
CREATE SEQUENCE  ts$post_comments_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
PROMPT Creating Sequence ts$roles_ID_SEQ ...
CREATE SEQUENCE  ts$roles_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
PROMPT Creating Sequence ts$messagequeue_ID_SEQ ...
CREATE SEQUENCE  ts$messagequeue_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Sequence ts$masters_ID_SEQ ...
CREATE SEQUENCE  ts$masters_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
PROMPT Creating Sequence ts$datastorequeue_ID_SEQ ...
CREATE SEQUENCE  ts$datastorequeue_ID_SEQ
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Table ts$post_types ...
CREATE TABLE ts$post_types (
  id NUMBER(10,0) NOT NULL,
  label VARCHAR2 (255)
);

ALTER TABLE ts$post_types ADD CONSTRAINT PK_ts$post_types PRIMARY KEY (ID) ENABLE;

INSERT INTO ts$post_types (id, label) VALUES (0, 'ServiceOrder');
INSERT INTO ts$post_types (id, label) VALUES (1, 'ServiceResponse');
INSERT INTO ts$post_types (id, label) VALUES (2, 'Info');
INSERT INTO ts$post_types (id, label) VALUES (3, 'BusinessModelUpdated');
INSERT INTO ts$post_types (id, label) VALUES (4, 'BusinessModelInstalled');
INSERT INTO ts$post_types (id, label) VALUES (5, 'UserPost');
  
PROMPT Creating Table ts$posts ...
CREATE TABLE ts$posts (
  id NUMBER(10,0) NOT NULL,
  create_date TIMESTAMP NOT NULL,
  type NUMBER(10,0) NOT NULL,
  title VARCHAR2(512 CHAR),
  body CLOB,
  body_target VARCHAR2(255 CHAR),
  wall_user_id NUMBER(10,0),
  target VARCHAR2(255 CHAR),
  target_code VARCHAR2(100 CHAR),
  tag VARCHAR2(100 CHAR)
);

ALTER TABLE ts$posts ADD CONSTRAINT PK_TS$POSTS PRIMARY KEY (ID) ENABLE;

CREATE INDEX IDX_ts$posts_create_date ON ts$posts (create_date);

ALTER TABLE ts$posts ADD CONSTRAINT FK_ts$posts_type FOREIGN KEY (type) REFERENCES ts$post_types (ID) ON DELETE CASCADE ENABLE;

PROMPT Creating Table ts$post_comments ...
CREATE TABLE ts$post_comments (
  id NUMBER(10,0) NOT NULL,
  create_date TIMESTAMP NOT NULL,
  id_author NUMBER(10,0) NOT NULL,
  author VARCHAR2(255 CHAR),
  text CLOB,
  id_post NUMBER(10,0) NOT NULL
);

ALTER TABLE ts$post_comments ADD CONSTRAINT PK_ts$post_comments PRIMARY KEY (id) ENABLE;
ALTER TABLE ts$post_comments ADD CONSTRAINT FK_ts$post_comments_posts FOREIGN KEY (id_post) REFERENCES ts$posts (ID) ON DELETE CASCADE ENABLE;

PROMPT Creating Table ts$post_user_filter ...
CREATE TABLE ts$post_user_filter (
  id_user NUMBER(10,0) NOT NULL,
  id_post NUMBER(10,0) NOT NULL
);

ALTER TABLE ts$post_user_filter ADD CONSTRAINT PK_ts$post_user_filter PRIMARY KEY (id_user, id_post) ENABLE;
ALTER TABLE ts$post_user_filter ADD CONSTRAINT FK_ts$post_user_filter FOREIGN KEY (id_post) REFERENCES ts$posts (ID) ON DELETE CASCADE ENABLE;

PROMPT Creating Table ts$post_user_author_filter ...
CREATE TABLE ts$post_user_author_filter (
  id_user NUMBER(10,0) NOT NULL,
  id_author NUMBER(10,0) NOT NULL
);

ALTER TABLE ts$post_user_author_filter ADD CONSTRAINT PK_ts$post_user_author_filter PRIMARY KEY (id_user, id_author) ENABLE;

PROMPT Creating Table ts$locations ...
CREATE TABLE ts$locations (
  id NUMBER(10,0) NOT NULL,
  id_node NUMBER(10,0) NOT NULL,
  id_location VARCHAR2(255 CHAR),
  geometry SDO_GEOMETRY NOT NULL,
  create_date TIMESTAMP(6)
);

CREATE INDEX ts$locations_idx_idnode ON ts$locations (ID_NODE);

INSERT INTO USER_SDO_GEOM_METADATA 
  VALUES (
  'ts$locations',
  'geometry',
  SDO_DIM_ARRAY(
    SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005),
    SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)
    ),
  8307
);

ALTER TABLE ts$locations ADD CONSTRAINT PK_TS$LOCATIONS PRIMARY KEY (ID) ENABLE;

CREATE INDEX ts$locations_idx
   ON ts$locations(geometry)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;
  
PROMPT Creating Table ts$workqueue ...
CREATE TABLE ts$workqueue (
  ID NUMBER(20) NOT NULL,
  TYPE NUMBER(20) NOT NULL,
  STATE NUMBER(20) NOT NULL,
  RETRIES NUMBER(20) NOT NULL,
  TARGET CLOB NOT NULL,
  LAST_ERROR CLOB,
  CREATION_TIME TIMESTAMP(6) NOT NULL,
  LAST_UPDATE_TIME TIMESTAMP(6) NOT NULL
);

ALTER TABLE ts$workqueue
ADD CONSTRAINT PK_TS$WORKQUEUE PRIMARY KEY
(
  ID
)
ENABLE
;
  
PROMPT Creating Table ts$eventlog ...
CREATE TABLE ts$eventlog (
  ID NUMBER(20) NOT NULL,
  LOGGER VARCHAR2(255 CHAR) NOT NULL,
  PRIORITY VARCHAR2(255 CHAR) NOT NULL,
  MESSAGE CLOB NOT NULL,
  STACKTRACE CLOB,
  CREATION_TIME TIMESTAMP(6) NOT NULL
);

ALTER TABLE ts$eventlog
ADD CONSTRAINT PRIMARY_EVENTLOG PRIMARY KEY
(
  ID
)
ENABLE
;

CREATE INDEX PRIORITY_EVENTLOG ON ts$eventlog
(
  PRIORITY
);

CREATE INDEX CREATION_TIME_EVENTLOG ON ts$eventlog
(
  CREATION_TIME
);

PROMPT Creating Table ts$notifications ...
CREATE TABLE ts$notifications ( 
  id NUMBER(20,0) NOT NULL ENABLE, 
  id_user NUMBER(10,0) NOT NULL ENABLE, 
  id_publication VARCHAR2(255 BYTE),
  label CLOB NOT NULL ENABLE, 
  icon CLOB, 
  read NUMBER(1,0) NOT NULL ENABLE,
  target VARCHAR2(255 BYTE), 
  create_date TIMESTAMP (6) NOT NULL ENABLE, 
  CONSTRAINT PRIMARY_NOTIFICATIONS PRIMARY KEY (id)
);

CREATE INDEX ID_PUBLICATION_NOTIFICATIONS ON ts$notifications (id_publication);

PROMPT Creating Table ts$default_values ...
CREATE TABLE ts$default_values (
  CODE_NODE VARCHAR2(100 CHAR) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL,
  PROPERTY VARCHAR2(100 CHAR) NOT NULL,
  DATA CLOB
);


PROMPT Creating Index CODE_NODE on ts$default_values ...
CREATE INDEX CODE_NODE ON ts$default_values
(
  CODE_NODE,
  ID_USER
) 
;
PROMPT Creating Index CODE_NODE_2 on ts$default_values ...
CREATE INDEX CODE_NODE_2 ON ts$default_values
(
  CODE_NODE,
  ID_USER,
  PROPERTY
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$default_values TO PUBLIC;


PROMPT Creating Table ts$logbooknode ...
CREATE TABLE ts$logbooknode (
  ID NUMBER(10,0) NOT NULL,
  HOST CLOB NOT NULL,
  LAYER CLOB NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL,
  ID_NODE NUMBER(10,0) NOT NULL,
  VISITED NUMBER(10,0) NOT NULL,
  CREATED NUMBER(10,0) NOT NULL,
  DELETED NUMBER(10,0) NOT NULL,
  MODIFIED NUMBER(10,0) NOT NULL,
  CREATE_DATE TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint PRIMARY_25 on table ts$logbooknode ... 
ALTER TABLE ts$logbooknode
ADD CONSTRAINT PRIMARY_25 PRIMARY KEY
(
  ID
)
ENABLE
;

PROMPT Creating Index ts$logbooknode_ID_NODE on ts$logbooknode ...
CREATE INDEX ts$logbooknode_ID_NODE ON ts$logbooknode (ID_NODE);

PROMPT Creating Index ts$logbooknode_ID_USER on ts$logbooknode ...
CREATE INDEX ts$logbooknode_ID_USER ON ts$logbooknode (ID_USER);

PROMPT Creating Index ts$logbooknode_VISITED on ts$logbooknode ...
CREATE INDEX ts$logbooknode_VISITED ON ts$logbooknode (VISITED);

PROMPT Creating Index ts$logbooknode_CREATED on ts$logbooknode ...
CREATE INDEX ts$logbooknode_CREATED ON ts$logbooknode (CREATED);

PROMPT Creating Index ts$logbooknode_DELETED on ts$logbooknode ...
CREATE INDEX ts$logbooknode_DELETED ON ts$logbooknode (DELETED);

PROMPT Creating Index ts$logbooknode_MODIFIED on ts$logbooknode ...
CREATE INDEX ts$logbooknode_MODIFIED ON ts$logbooknode (MODIFIED);

PROMPT Creating Index ts$logbooknode_CREATE_DATE on ts$logbooknode ...
CREATE INDEX ts$logbooknode_CREATE_DATE ON ts$logbooknode (CREATE_DATE);

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$logbooknode TO PUBLIC;

PROMPT Creating Table ts$logbooknode_subscribers ...
CREATE TABLE ts$logbooknode_subscribers (
  SERVER_NAME CLOB NOT NULL,
  SERVER_HOST CLOB NOT NULL,
  SERVER_PORT NUMBER(10,0) NOT NULL,
  TYPE NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL,
  REGISTER_DATE TIMESTAMP(6) NOT NULL
);


GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$logbooknode_subscribers TO PUBLIC;

PROMPT Creating Table ts$nodes ...
CREATE TABLE ts$nodes (
  ID NUMBER(10,0) NOT NULL,
  ID_PARENT NUMBER(10,0),
  ID_OWNER NUMBER(10,0),
  ID_PROTOTYPE NUMBER(10,0),
  CODE VARCHAR2(100 CHAR) NOT NULL,
  DEFINITION_TYPE VARCHAR2(100 CHAR) NOT NULL,
  PARTNER_CONTEXT VARCHAR2(100 CHAR),
  ORDERING NUMBER(10,0) NOT NULL,
  DATA CLOB,
  FLAGS CLOB,
  NOTES CLOB,
  VALID NUMBER(10,0) DEFAULT 0 NULL
);


PROMPT Creating Primary Key Constraint TS$NODES_ID on table ts$nodes ... 
ALTER TABLE ts$nodes
ADD CONSTRAINT TS$NODES_ID PRIMARY KEY
(
  ID
)
ENABLE
;

PROMPT Creating Index TS$NODES_IDPARENT on ts$nodes ...
CREATE INDEX TS$NODES_IDPARENT ON ts$nodes
(
  ID_PARENT
) 
;

PROMPT Creating Index TS$NODES_IDOWNER on ts$nodes ...
CREATE INDEX TS$NODES_IDOWNER ON ts$nodes
(
  ID_OWNER
) 
;

PROMPT Creating Index TS$NODES_IDPROTOTYPE on ts$nodes ...
CREATE INDEX TS$NODES_IDPROTOTYPE ON ts$nodes
(
  ID_PROTOTYPE
) 
;

PROMPT Creating Index TS$NODES_CODE on ts$nodes ...
CREATE INDEX TS$NODES_CODE ON ts$nodes
(
  CODE
) 
;

PROMPT Creating Index TS$NODES_DEFINITION_TYPE on ts$nodes ...
CREATE INDEX TS$NODES_DEFINITION_TYPE ON ts$nodes
(
  DEFINITION_TYPE
) 
;

PROMPT Creating Index 
CREATE INDEX TS$NODES_DATA ON ts$nodes(data)
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
begin 
  DBMS_STATS.GATHER_TABLE_STATS(USER, 'ts$nodes', cascade=>TRUE); 
end;

/
  
PROMPT Creating Index TS$NODES_IDNIDP on ts$nodes ...
CREATE INDEX TS$NODES_IDNIDP ON ts$nodes
(
  ID_PROTOTYPE, ID
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes TO PUBLIC;

PROMPT Creating Table ts$node_ancestors ...
CREATE TABLE ts$nodes_ancestors (
  ID_ANCESTOR NUMBER(10,0) NOT NULL,
  ID_NODE NUMBER(10,0) NOT NULL,
  ANCESTOR_LEVEL NUMBER(10,0) NOT NULL
);

CREATE INDEX TS$NODES_ANCESTORS_ID_ANCE ON ts$nodes_ancestors (ID_ANCESTOR);
CREATE INDEX TS$NODES_ANCESTORS_ID_NODE ON ts$nodes_ancestors (ID_NODE);

ALTER TABLE ts$nodes_ancestors ADD CONSTRAINT FK_ts$ancestors_id_ancestor FOREIGN KEY (ID_ANCESTOR) REFERENCES ts$nodes (ID) ON DELETE CASCADE ENABLE;
ALTER TABLE ts$nodes_ancestors ADD CONSTRAINT FK_ts$ancestors_id_node FOREIGN KEY (ID_NODE) REFERENCES ts$nodes (ID) ON DELETE CASCADE ENABLE;

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_ancestors TO PUBLIC;


PROMPT Creating Table ts$nodes_descriptors ...
CREATE TABLE ts$nodes_descriptors (
  ID_NODE NUMBER(10,0),
  ID_OWNER NUMBER(10,0),
  ID_PARENT NUMBER(10,0),
  CODE VARCHAR2(255 CHAR),
  LABEL VARCHAR2(4000 CHAR),
  DESCRIPTION VARCHAR2(4000 CHAR),
  COLOR VARCHAR2(20 CHAR),
  ORDERING NUMBER(10,0),
  CREATE_DATE TIMESTAMP(6),
  UPDATE_DATE TIMESTAMP(6),
  DELETE_DATE TIMESTAMP(6),
  HIGHLIGHTED VARCHAR2(5 CHAR),
  EDITABLE VARCHAR(5 CHAR),
  DELETABLE VARCHAR(5 CHAR),
  PROTOTYPE NUMBER(10,0) DEFAULT 0,
  GEOREFERENCED NUMBER(10,0) DEFAULT -1
);

PROMPT Creating Index TS$NODES_DESCRIPTORS_LABEL on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_LABE ON ts$nodes_descriptors(label) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT) WORDLIST wildcard_pref');
  
PROMPT Creating Index TS$NODES_DESCRIPTORS_DDP on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_DDP ON ts$nodes_descriptors(delete_date,prototype);

PROMPT Creating Index TS$NODES_DESCRIPTORS_DESCRIPTION on ts$nodes_descriptors ...  
CREATE INDEX TS$NODES_DESCRIPTORS_DESC ON ts$nodes_descriptors(description) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
begin 
  DBMS_STATS.GATHER_TABLE_STATS(USER, 'ts$nodes_descriptors', cascade=>TRUE); 
end;

/

PROMPT Creating Index TS$NODES_DESCRIPTORS_CODE on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_CODE ON ts$nodes_descriptors
(
  CODE
)
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_CREATEDAT on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_CREATEDAT ON ts$nodes_descriptors
(
  CREATE_DATE
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_DELETEDAT on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_DELETEDAT ON ts$nodes_descriptors
(
  DELETE_DATE
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_HIGHTED on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_HIGHTED ON ts$nodes_descriptors
(
  HIGHLIGHTED
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_IDNODE on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_IDNODE ON ts$nodes_descriptors
(
  ID_NODE
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_IDOWNER on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_IDOWNER ON ts$nodes_descriptors
(
  ID_OWNER
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_LABEL on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_LABEL ON ts$nodes_descriptors
(
  LABEL
)
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_ORD_1 on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_ORD_1 ON ts$nodes_descriptors
(
  DESCRIPTION
)
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_UPDATEDAT on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_UPDATEDAT ON ts$nodes_descriptors
(
  UPDATE_DATE
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_IDPARENT on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_IDPARENT ON ts$nodes_descriptors
(
  ID_PARENT
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_PROTOTYPE on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_PROTOTYPE ON ts$nodes_descriptors
(
  PROTOTYPE
) 
;
PROMPT Creating Index TS$NODES_DESCRIPTORS_ISGEOREF on ts$nodes_descriptors ...
CREATE INDEX TS$NODES_DESCRIPTORS_GEOREF ON ts$nodes_descriptors
(
  GEOREFERENCED
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_descriptors TO PUBLIC;


PROMPT Creating Table ts$nodes_permissions ...
CREATE TABLE ts$nodes_permissions (
  ID NUMBER(10,0) NOT NULL,
  ID_NODE NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0),
  TYPE NUMBER(10,0) DEFAULT '1' NOT NULL,
  BEGIN_DATE TIMESTAMP(6) NOT NULL,
  EXPIRE_DATE TIMESTAMP(6)
);

PROMPT Creating Primary Key Constraint PRIMARY_2 on table ts$nodes_permissions ... 
ALTER TABLE ts$nodes_permissions
ADD CONSTRAINT PRIMARY_2 PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index TS$NODES_PERMS_INDEX_IDNODE on ts$nodes_permissions ...
CREATE INDEX TS$NODES_PERMS_INDEX_IDNODE ON ts$nodes_permissions
(
  ID_NODE
) 
;
PROMPT Creating Index TS$NODES_PERMS_INDEX_IDUSER on ts$nodes_permissions ...
CREATE INDEX TS$NODES_PERMS_INDEX_IDUSER ON ts$nodes_permissions
(
  ID_USER
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_permissions TO PUBLIC;


PROMPT Creating Table ts$nodes_revisions ...
CREATE TABLE ts$nodes_revisions (
  ID NUMBER(10,0) NOT NULL,
  ID_NODE NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0),
  DATA CLOB,
  MERGED NUMBER(10,0),
  REVISION_DATE TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint ts$nodes_revisions_PRIMARY on table ts$nodes_revisions ... 
ALTER TABLE ts$nodes_revisions
ADD CONSTRAINT ts$nodes_revisions_PRIMARY PRIMARY KEY
(
  ID
)
ENABLE
;

PROMPT Creating Index ts$nodes_revisions_id_node_idx on ts$nodes_revisions ...
CREATE INDEX ts$nodes_revisions_id_node_idx ON ts$nodes_revisions
(
  ID_NODE
) 
;
PROMPT Creating Index ts$nodes_revisions_id_user_idx on ts$nodes_revisions ...
CREATE INDEX ts$nodes_revisions_id_user_idx ON ts$nodes_revisions
(
  ID_USER
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_revisions TO PUBLIC;


PROMPT Creating Table ts$nodes_schemas ...
CREATE TABLE ts$nodes_schemas (
  ID_NODE NUMBER(10,0) NOT NULL,
  DATA CLOB
);

CREATE INDEX TS$NODES_SCHEMAS_ID_NODE ON ts$nodes_schemas (ID_NODE);

PROMPT Creating Primary Key Constraint PRIMARY_13 on table ts$nodes_schemas ... 
ALTER TABLE ts$nodes_schemas
ADD CONSTRAINT PRIMARY_13 PRIMARY KEY
(
  ID_NODE
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_schemas TO PUBLIC;


PROMPT Creating Table ts$nodes_superdata ...
CREATE TABLE ts$nodes_superdata (
  ID NUMBER(10,0) NOT NULL,
  ID_NODE NUMBER(10,0) NOT NULL,
  CODE_ATTRIBUTE VARCHAR2(255 CHAR) NOT NULL,
  CODE CLOB,
  VALUE CLOB,
  DATA CLOB
);


PROMPT Creating Primary Key Constraint PRIMARY_20 on table ts$nodes_superdata ... 
ALTER TABLE ts$nodes_superdata
ADD CONSTRAINT PRIMARY_20 PRIMARY KEY
(
  ID,
  ID_NODE
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_superdata TO PUBLIC;

PROMPT Creating Table ts$nodes_superdata_revisions ...
CREATE TABLE ts$nodes_superdata_revisions (
  ID_NODE NUMBER(10,0) NOT NULL,
  ID_SUPER_DATA NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0),
  CODE_ATTRIBUTE VARCHAR2(255 CHAR) NOT NULL,
  CODE CLOB,
  VALUE CLOB,
  DATA CLOB,
  MERGED NUMBER(10,0),
  REVISION_DATE TIMESTAMP(6) NOT NULL
);


PROMPT Creating Index ts$nods_sd_revisions_idnod_idx on ts$nodes_superdata_revisions ...
CREATE INDEX ts$nods_sd_revisions_idnod_idx ON ts$nodes_superdata_revisions
(
  ID_NODE
) 
;
PROMPT Creating Index ts$nods_sd_rvisins_idsprdt_idx on ts$nodes_superdata_revisions ...
CREATE INDEX ts$nods_sd_rvisins_idsprdt_idx ON ts$nodes_superdata_revisions
(
  ID_SUPER_DATA
) 
;
PROMPT Creating Index ts$nods_sd_revisions_idusr_idx on ts$nodes_superdata_revisions ...
CREATE INDEX ts$nods_sd_revisions_idusr_idx ON ts$nodes_superdata_revisions
(
  ID_USER
) 
;
PROMPT Creating Index ts$nods_sd_revisio_codeatt_idx on ts$nodes_superdata_revisions ...
CREATE INDEX ts$nods_sd_revisio_codeatt_idx ON ts$nodes_superdata_revisions
(
  CODE_ATTRIBUTE
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$nodes_superdata_revisions TO PUBLIC;

PROMPT Creating Table ts$links ...
CREATE TABLE ts$links (
  ID_SOURCE NUMBER(10,0) NOT NULL,
  ID_SOURCE_COMPONENT NUMBER(10,0),
  TYPE_SOURCE VARCHAR2(20 CHAR) NOT NULL,
  ID_TARGET NUMBER(10,0) NOT NULL,
  TYPE_TARGET VARCHAR2(20 CHAR) NOT NULL,
  DATA VARCHAR2(255 CHAR) NOT NULL,
  DELETE_DATE TIMESTAMP(6)
);

PROMPT Creating Index TS$LINKS_INDEX1 on ts$links ...
CREATE INDEX TS$LINKS_INDEX1 ON ts$links
(
  ID_SOURCE
) 
;
PROMPT Creating Index TS$LINKS_INDEX2 on ts$links ...
CREATE INDEX TS$LINKS_INDEX2 ON ts$links
(
  TYPE_SOURCE
) 
;
PROMPT Creating Index TS$LINKS_INDEX3 on ts$links ...
CREATE INDEX TS$LINKS_INDEX3 ON ts$links
(
  ID_TARGET
) 
;
PROMPT Creating Index TS$LINKS_INDEX4 on ts$links ...
CREATE INDEX TS$LINKS_INDEX4 ON ts$links
(
  TYPE_TARGET
) 
;
PROMPT Creating Index TS$LINKS_INDEX5 on ts$links ...
CREATE INDEX TS$LINKS_INDEX5 ON ts$links
(
  ID_SOURCE_COMPONENT
)
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$links TO PUBLIC;

PROMPT Creating Table ts$attachments ...
CREATE TABLE ts$attachments (
  ID_SOURCE NUMBER(10,0) NOT NULL,
  ID_TARGET VARCHAR2(255) NOT NULL,
  TYPE_TARGET VARCHAR2(20 CHAR) NOT NULL,
  CODE_TARGET VARCHAR2(100 CHAR) NOT NULL,
  DATA VARCHAR2(255 CHAR) NOT NULL,
  DELETE_DATE TIMESTAMP(6)
);

PROMPT Creating ts$attachments pk...
ALTER TABLE ts$attachments ADD CONSTRAINT PRIMARY_ATTACH_1 PRIMARY KEY (ID_SOURCE, ID_TARGET, TYPE_TARGET) ENABLE;

PROMPT Creating ts$attachments indexes...
CREATE INDEX TS$ATTACHMENTS_INDEX1 ON ts$attachments (ID_SOURCE);
CREATE INDEX TS$ATTACHMENTS_INDEX3 ON ts$attachments (ID_TARGET); 
CREATE INDEX TS$ATTACHMENTS_INDEX4 ON ts$attachments (TYPE_TARGET);
CREATE INDEX TS$ATTACHMENTS_INDEX5 ON ts$attachments (CODE_TARGET);
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$attachments TO PUBLIC;

PROMPT Creating Table ts$sequences ...
CREATE TABLE ts$sequences (
  CODE VARCHAR2(255 CHAR) NOT NULL,
  CODE_SUBSEQUENCE VARCHAR2(255 CHAR),
  VALUE NUMBER(10,0) DEFAULT '1' NOT NULL
);


PROMPT Creating Primary Key Constraint PRIMARY_21 on table ts$sequences ... 
ALTER TABLE ts$sequences
ADD CONSTRAINT PRIMARY_21 PRIMARY KEY
(
  CODE
)
ENABLE
;
PROMPT Creating Index TS$SEQUENCES_INDEX_CODE_SUBSEQ on ts$sequences ...
CREATE INDEX TS$SEQUENCES_INDEX_CODE_SUBSEQ ON ts$sequences
(
  CODE_SUBSEQUENCE
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$sequences TO PUBLIC;

PROMPT Creating Table ts$services ...
CREATE TABLE ts$services (
  ID NUMBER(10,0) NOT NULL,
  CODE CLOB,
  REMOTE_UNIT_LABEL VARCHAR2(255 CHAR),
  LOCAL_MAILBOX CLOB,
  REPLY_MAILBOX CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  ID_TASK NUMBER(10,0) NULL
);


PROMPT Creating Primary Key Constraint ts$services_PK on table ts$services ... 
ALTER TABLE ts$services
ADD CONSTRAINT ts$services_PK PRIMARY KEY
(
  ID
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$services TO PUBLIC;

PROMPT Creating Table ts$tasks ...
CREATE TABLE ts$tasks (
  ID NUMBER(10,0) NOT NULL,
  ID_OWNER NUMBER(10,0),
  OWNER_FULLNAME VARCHAR2(200 CHAR),
  ID_SENDER NUMBER(10,0),
  SENDER_FULLNAME VARCHAR2(200 CHAR),
  ID_TARGET NUMBER(10,0),
  PARTNER_CONTEXT VARCHAR2(100 CHAR),
  CODE_ROLE VARCHAR2(255 CHAR),
  LABEL VARCHAR2(4000 CHAR),
  DESCRIPTION CLOB,
  SHORTCUTS VARCHAR2(4000 CHAR),
  CODE VARCHAR2(100 CHAR),
  STATE VARCHAR2(1000 CHAR),
  URGENT NUMBER(10,0) DEFAULT 0,
  CLASSIFICATOR VARCHAR2(200 CHAR),
  INITIALIZER NUMBER(10,0) DEFAULT 0,
  BACKGROUND NUMBER(10,0) DEFAULT 0,
  NEW_MESSAGES NUMBER(20,0) DEFAULT '0',
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  AVAILABLE_DATE TIMESTAMP(6),
  UPDATE_DATE TIMESTAMP(6),
  START_DATE TIMESTAMP(6),
  SUGGESTED_START_DATE TIMESTAMP(6),
  END_DATE TIMESTAMP(6),
  SUGGESTED_END_DATE TIMESTAMP(6),
  COMMENTS CLOB,
  GEOMETRY SDO_GEOMETRY,
  GEOMETRY_LABEL CLOB,
  DATA CLOB
);

INSERT INTO USER_SDO_GEOM_METADATA 
  VALUES (
  'ts$tasks',
  'geometry',
  SDO_DIM_ARRAY(
    SDO_DIM_ELEMENT('Longitude', -180, 180, 0.005),
    SDO_DIM_ELEMENT('Latitude', -90, 90, 0.005)
    ),
  8307
);

CREATE INDEX ts$tasks_idx
   ON ts$tasks(geometry)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;

PROMPT Creating Index TS$TASKS_IDOWNER on ts$tasks ...
CREATE INDEX TS$TASKS_IDOWNER ON ts$tasks (ID_OWNER);

PROMPT Creating Index TS$TASKS_IDSENDER on ts$tasks ...
CREATE INDEX TS$TASKS_IDSENDER ON ts$tasks (ID_SENDER);

PROMPT Creating Index TS$TASKS_CODEROLE on ts$tasks ...
CREATE INDEX TS$TASKS_CODEROLE ON ts$tasks (CODE_ROLE);

PROMPT Creating Index TS$TASKS_TYPE_CLASS on ts$tasks ...
CREATE INDEX TS$TASKS_TYPE_CLASS ON ts$tasks (CODE, CLASSIFICATOR);

PROMPT Creating Index TS$TASKS_TYPE_STARTDATE on ts$tasks ...
CREATE INDEX TS$TASKS_TYPE_STARTDATE ON ts$tasks (START_DATE);

PROMPT Creating Index TS$TASKS_AVAILABLEDATE on ts$tasks ...
CREATE INDEX TS$TASKS_AVAILABLEDATE ON ts$tasks (AVAILABLE_DATE);

PROMPT Creating Index TS$TASKS_TYPE_SUGGESTEDSTARTDA on ts$tasks ...
CREATE INDEX TS$TASKS_TYPE_SUGGESTEDSTARTDA ON ts$tasks (SUGGESTED_START_DATE);

PROMPT Creating Index TS$TASKS_TYPE_ENDDATE on ts$tasks ...
CREATE INDEX TS$TASKS_TYPE_ENDDATE ON ts$tasks (END_DATE);

PROMPT Creating Index TS$TASKS_TYPE_SUGGESTEDENDDATE on ts$tasks ...
CREATE INDEX TS$TASKS_TYPE_SUGGESTEDENDDATE ON ts$tasks (SUGGESTED_END_DATE);

PROMPT Creating Index TS$TASKS_LABEL on ts$tasks ...
CREATE INDEX TS$TASKS_LABEL_IDX ON ts$tasks(label) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
PROMPT Creating Index TS$TASKS_DESCRIPTION on ts$tasks ...  
CREATE INDEX TS$TASKS_DESCRIP_IDX ON ts$tasks(description) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
PROMPT Creating Index TS$TASKS_DATA on ts$tasks ...  
CREATE INDEX TS$TASKS_DATA_IDX ON ts$tasks(data) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
begin 
  DBMS_STATS.GATHER_TABLE_STATS(USER, 'ts$tasks', cascade=>TRUE); 
end;

/

PROMPT Creating Primary Key Constraint PRIMARY_16 on table ts$tasks ... 
ALTER TABLE ts$tasks
ADD CONSTRAINT PRIMARY_16 PRIMARY KEY
(
  ID
)
ENABLE
;

PROMPT Creating Index TS$TASKS_INDEX_IDNODE on ts$tasks ...
CREATE INDEX TS$TASKS_INDEX_IDNODE ON ts$tasks
(
  ID_TARGET
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks TO PUBLIC;

PROMPT Creating Table ts$tasks_jobs ...
CREATE TABLE ts$tasks_jobs (
  ID_TASK NUMBER NOT NULL, 
  REQUEST BLOB, 
  RESPONSE BLOB, 
  CALLBACK_TASK_ID NUMBER, 
  CALLBACK_CODE VARCHAR2(100 CHAR), 
  CALLBACK_ORDER_ID NUMBER, 
  CREATE_DATE TIMESTAMP(6) NOT NULL, 
  FINISH_DATE TIMESTAMP(6), 
  CONSTRAINT TS$TASKS_JOBS_PK PRIMARY KEY (ID_TASK)
);

ALTER TABLE TS$TASKS_JOBS
ADD CONSTRAINT TS$TASKS_JOBS_TS$TASKS_FK1 FOREIGN KEY (ID_TASK) REFERENCES TS$TASKS (ID)
ON DELETE CASCADE ENABLE;

CREATE INDEX TS$TASKS_JOBS_CREATEDATE ON TS$TASKS_JOBS (CREATE_DATE);

PROMPT Creating Table ts$tasks_jobs_attachments ...
CREATE TABLE ts$tasks_jobs_attachments (
  ID_TASK NUMBER NOT NULL,
  ID_ATTACHMENT VARCHAR2(255 CHAR),
  CONSTRAINT TS$TASKS_JOBS_ATTACHMENTS_PK PRIMARY KEY (ID_TASK, ID_ATTACHMENT)
);

PROMPT Creating Table ts$tasks_orders ...
CREATE TABLE ts$tasks_orders (
  ID NUMBER(10,0) NOT NULL,
  ID_TASK NUMBER(10,0),
  ID_SETUP_NODE NUMBER(10,0),
  ID_ROLE NUMBER(10,0),
  CODE VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(255 CHAR) NOT NULL,
  PARTNER_CONTEXT VARCHAR2(100 CHAR),
  COMMENTS CLOB,
  URGENT NUMBER(10,0) DEFAULT '0',
  CLOSED NUMBER(10,0) DEFAULT '0',
  NEW_MESSAGES NUMBER(20) DEFAULT '0',
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  SUGGESTED_START_DATE TIMESTAMP(6),
  SUGGESTED_END_DATE TIMESTAMP(6)
);

PROMPT Creating Primary Key Constraint ts$tasks_orders_primary on table ts$tasks_orders ... 
ALTER TABLE ts$tasks_orders ADD CONSTRAINT ts$tasks_orders_primary PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index TS$TASKSORDERS_IDTASK on ts$tasks_orders ...
CREATE INDEX TS$TASKSORDERS_IDTASK ON ts$tasks_orders (ID_TASK);

PROMPT Creating Index TS$TASKSORDERS_COMMENTS on ts$tasks_orders ...  
CREATE INDEX TS$TASKSORDERS_COM_IDX ON ts$tasks_orders(comments) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks_orders TO PUBLIC;


PROMPT Creating Table ts$tasks_orders_chats_entries ...
CREATE TABLE ts$tasks_orders_chats_entries (
  ID NUMBER(10,0) NOT NULL,
  ID_ORDER NUMBER(10,0),
  MESSAGE CLOB,
  TYPE VARCHAR2(255 CHAR) NOT NULL,
  SENT NUMBER(1,0) DEFAULT '0',
  CREATE_DATE TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint ts$TASKSORDERSCHATENTRIES_PRIMARY on table ts$tasks_orders ... 
ALTER TABLE ts$tasks_orders_chats_entries ADD CONSTRAINT ts$tasks_orders_chats_primary PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index TS$TASKSORDERSCHATENTRIES_IDORDER on ts$tasks_orders_chats_entries ...
CREATE INDEX TS$TASKSORDERSCHAT_IDORDER ON ts$tasks_orders_chats_entries (ID_ORDER);

PROMPT Creating Index TS$TASKSORDERSCHATSENTRIES_MESSAGE on ts$tasks_orders_chats_entries ...  
CREATE INDEX TS$TASKS_O_C_MSG_IDX ON ts$tasks_orders_chats_entries(MESSAGE) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks_orders_chats_entries TO PUBLIC;



PROMPT Creating Table ts$tasks_definition ...
CREATE TABLE ts$tasks_definition (
  CODE VARCHAR2(7 CHAR),
  LABEL CLOB,
  LANGUAGE VARCHAR(7 CHAR)
);

PROMPT Creating Primary Key Constraint TS$TASKS_DEFINITION_CODE on table ts$tasks_definition ... 
ALTER TABLE ts$tasks_definition
ADD CONSTRAINT TS$TASKS_DEFINITION_CODE PRIMARY KEY
(
  CODE,
  LANGUAGE
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks_definition TO PUBLIC;

PROMPT Creating Table ts$tasks_facts ...
CREATE TABLE ts$tasks_facts (
  ID NUMBER(10,0) NOT NULL,
  ID_TASK NUMBER(10,0) NOT NULL,
  ID_USER VARCHAR2(4000 CHAR) NOT NULL,
  TITLE CLOB,
  SUBTITLE CLOB,
  EXTRA_DATA CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint PRIMARY_10 on table ts$tasks_facts ... 
ALTER TABLE ts$tasks_facts
ADD CONSTRAINT PRIMARY_10 PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index ID_TASK_1 on ts$tasks_facts ...
CREATE INDEX ID_TASK_1 ON ts$tasks_facts
(
  ID_TASK
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks_facts TO PUBLIC;


PROMPT Creating Table ts$tasks_timers ...
CREATE TABLE ts$tasks_timers (
  ID NUMBER(10,0) NOT NULL,
  ID_TASK NUMBER(10,0) NOT NULL,
  TAG VARCHAR2(255 CHAR) NOT NULL,
  DELAY NUMBER(20,0) DEFAULT '0' NOT NULL,
  CREATE_DATE TIMESTAMP(6) NOT NULL
);


PROMPT Creating Primary Key Constraint ts$tasks_timers_PK on table ts$tasks_timers ... 
ALTER TABLE ts$tasks_timers
ADD CONSTRAINT ts$tasks_timers_PK PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index ts$tasks_timers_IDX_IDTASK on ts$tasks_timers ...
CREATE INDEX ts$tasks_timers_IDX_IDTASK ON ts$tasks_timers
(
  ID_TASK
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks_timers TO PUBLIC;

PROMPT Creating Table ts$tasks_snapshots ...
CREATE TABLE ts$tasks_snapshots (
  ID NUMBER(10,0) NOT NULL,
  ID_TASK NUMBER(10,0) NOT NULL,
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  DATA CLOB
);


PROMPT Creating Primary Key Constraint ts$tasks_snapshots_PK on table ts$tasks_snapshots ... 
ALTER TABLE ts$tasks_snapshots
ADD CONSTRAINT ts$tasks_snapshots_PK PRIMARY KEY
(
  ID
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$tasks_snapshots TO PUBLIC;

PROMPT Creating Table ts$mailboxes ...
CREATE TABLE ts$mailboxes (
  ID VARCHAR2(255 CHAR) NOT NULL,
  ID_TASK NUMBER(10,0) NOT NULL,
  CODE VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(255 CHAR) NOT NULL
);


PROMPT Creating Primary Key Constraint ts$mailboxes_PK on table ts$mailboxes ... 
ALTER TABLE ts$mailboxes
ADD CONSTRAINT ts$mailboxes_PK PRIMARY KEY
(
  ID
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$mailboxes TO PUBLIC;

PROMPT Creating Table ts$mailboxes_permissions ...
CREATE TABLE ts$mailboxes_permissions (
  ID VARCHAR2(255 CHAR) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL
);

PROMPT Creating Primary Key Constraint ts$mailboxes_permissions_PK on table ts$mailboxes ... 
ALTER TABLE ts$mailboxes_permissions
ADD CONSTRAINT ts$mailboxes_permissions_PK PRIMARY KEY ( ID, ID_USER ) ENABLE;

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$mailboxes_permissions TO PUBLIC;

PROMPT Creating Table ts$cubes ...
CREATE TABLE ts$cubes (
  ID NUMBER(10,0) NOT NULL,
  TYPE VARCHAR2(100 CHAR) NOT NULL,
  LABEL CLOB,
  DESCRIPTION CLOB,
  DATA CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  UPDATE_DATE TIMESTAMP(6)
);

PROMPT Creating Index TS$CUBES_TYPE on ts$cubes ...
CREATE INDEX TS$CUBES_TYPE ON ts$cubes(TYPE);

PROMPT Creating Index TS$CUBES_LABEL on ts$cubes ...
CREATE INDEX TS$CUBES_LABEL_IDX ON ts$cubes(label) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
PROMPT Creating Index TS$CUBES_DESCRIPTION on ts$cubes ...  
CREATE INDEX TS$CUBES_DESCRIP_IDX ON ts$cubes(description) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
PROMPT Creating Index TS$CUBES_DATA on ts$cubes ...  
CREATE INDEX TS$CUBES_DATA_IDX ON ts$cubes(data) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
  
begin 
  DBMS_STATS.GATHER_TABLE_STATS(USER, 'ts$cubes', cascade=>TRUE); 
end;

/

PROMPT Creating Primary Key Constraint PK_TS$CUBES_ID on table ts$cubes ... 
ALTER TABLE ts$cubes
ADD CONSTRAINT PK_TS$CUBES_ID PRIMARY KEY
(
  ID
)
ENABLE
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$cubes TO PUBLIC;

PROMPT Creating Table ts$cubes_reports ...
CREATE TABLE ts$cubes_reports (
  ID NUMBER(10,0) NOT NULL,
  ID_CUBE NUMBER(10,0) NOT NULL,
  LABEL CLOB,
  DESCRIPTION CLOB,
  DATA CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  UPDATE_DATE TIMESTAMP(6),
  IS_VALID NUMBER(10,0) DEFAULT '0'
);

PROMPT Creating Primary Key Constraint PK_TS$CUBES_REPORTS_ID on table ts$cubes_reports ... 
ALTER TABLE ts$cubes_reports
ADD CONSTRAINT PK_TS$CUBES_REPORTS_ID PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index ID_CUBE on ts$cubes_reports ...
CREATE INDEX ID_CUBE ON ts$cubes_reports
(
  ID_CUBE
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$cubes_reports TO PUBLIC;

PROMPT Creating Table ts$cubes_filtergroups ...
CREATE TABLE ts$cubes_filtergroups (
  ID NUMBER(10,0) NOT NULL,
  ID_CUBE NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL,
  LABEL VARCHAR2(255 CHAR) NOT NULL,
  DATA CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint PK_TS$CUBES_FILTERGROUPS_ID on table ts$cubes_filtergroups ... 
ALTER TABLE ts$cubes_filtergroups ADD CONSTRAINT PK_TS$CUBES_FILTERGROUPS_ID PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index LABEL on ts$cubes_filtergroups ...
CREATE INDEX LABEL ON ts$cubes_filtergroups (LABEL); 

PROMPT Creating Index ID_CUBE on ts$cubes_filtergroups ...
CREATE INDEX IDX_CUBES_FILTERGROUPS_ID_CUBE ON ts$cubes_filtergroups (ID_CUBE); 

PROMPT Creating Index ID_USER on ts$cubes_filtergroups ...
CREATE INDEX IDX_CUBES_FILTERGROUPS_ID_USER ON ts$cubes_filtergroups (ID_USER);

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$cubes_filtergroups TO PUBLIC;

PROMPT Creating Table ts$sources ...
CREATE TABLE ts$sources (
  ID NUMBER(10,0) NOT NULL,
  CODE VARCHAR2(255 CHAR) NOT NULL,
  NAME VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(255 CHAR) NOT NULL,
  ONTOLOGY VARCHAR2(255 CHAR) NOT NULL,
  PARTNER_NAME VARCHAR2(255 CHAR),
  PARTNER_LABEL VARCHAR2(255 CHAR),
  URI VARCHAR2(255 CHAR),
  LABEL CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL,
  UPDATE_DATE TIMESTAMP(6) NOT NULL,
  ENABLED VARCHAR2(5 CHAR) 
);


PROMPT Creating Primary Key Constraint TS$SOURCES_PK on table ts$sources ... 
ALTER TABLE ts$sources
ADD CONSTRAINT TS$SOURCES_PK PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index TS$SOURCES_CODE_1 on ts$sources ...
CREATE INDEX TS$SOURCES_CODE_1 ON ts$sources
(
  CODE
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$sources TO PUBLIC;


PROMPT Creating Table ts$users ...
CREATE TABLE ts$users (
  ID NUMBER(10,0) NOT NULL,
  USERNAME VARCHAR2(200 CHAR) NOT NULL,
  FULLNAME VARCHAR2(255 CHAR),
  EMAIL CLOB,
  PHOTO CLOB,
  PREFERENCES CLOB,
  REGISTER_DATE TIMESTAMP(6) NOT NULL
);

INSERT INTO "TS$USERS" (ID, USERNAME, FULLNAME, EMAIL, PREFERENCES, REGISTER_DATE) VALUES (-1, 'system', 'Sistema', 'system@yourcompany.com', null, TO_DATE('04/06/10', 'DD/MM/RR'));

PROMPT Creating Primary Key Constraint PRIMARY_4 on table ts$users ... 
ALTER TABLE ts$users
ADD CONSTRAINT PRIMARY_4 PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index TS$USERS_INDEX_NAME on ts$users ...
CREATE INDEX TS$USERS_INDEX_USERNAME ON ts$users (USERNAME);
CREATE INDEX TS$USERS_USERNAME_FT ON ts$users (username) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$users TO PUBLIC;


PROMPT Creating Table ts$users_nodes ...
CREATE TABLE ts$users_nodes (
  ID_USER NUMBER(10,0) NOT NULL,
  ID_NODE NUMBER(10,0) NOT NULL,
  ROLE VARCHAR2(255) NOT NULL
);


PROMPT Creating Primary Key Constraint PRIMARY_12 on table ts$users_nodes ... 
ALTER TABLE ts$users_nodes
ADD CONSTRAINT PRIMARY_12 PRIMARY KEY
(
  ID_USER,
  ID_NODE,
  ROLE
)
ENABLE
;
PROMPT Creating Index TS$USERS_NODES_INDEX_IDNODE on ts$users_nodes ...
CREATE INDEX TS$USERS_NODES_INDEX_IDNODE ON ts$users_nodes
(
  ID_NODE
) 
;
PROMPT Creating Index TS$USERS_NODES_INDEX_IDUSER on ts$users_nodes ...
CREATE INDEX TS$USERS_NODES_INDEX_IDUSER ON ts$users_nodes
(
  ID_USER
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$users_nodes TO PUBLIC;
PROMPT Creating Index TS$USERS_NODES_INDEX_ROLE on ts$users_nodes ...
CREATE INDEX TS$USERS_NODES_INDEX_ROLE ON ts$users_nodes
(
  ROLE
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$users_nodes TO PUBLIC;

PROMPT Creating Table ts$users_dashboards ...
CREATE TABLE ts$users_dashboards (
  ID_USER NUMBER(10,0) NOT NULL,
  ID_DASHBOARD VARCHAR2(50) NOT NULL,
  ROLE VARCHAR2(255) NOT NULL
);

PROMPT Creating Primary Key Constraint PRIMARY_14 on table ts$users_dashboards ... 
ALTER TABLE ts$users_dashboards
ADD CONSTRAINT PRIMARY_14 PRIMARY KEY
(
  ID_USER,
  ID_DASHBOARD,
  ROLE
)
ENABLE
;
PROMPT Creating Index TS$USERS_DASHBOARDS_INDEX_IDDASHBOARD on ts$users_dashboards ...
CREATE INDEX TS$USERS_DASHB_INDEX_IDDASHB ON ts$users_dashboards
(
  ID_DASHBOARD
) 
;
PROMPT Creating Index TS$USERS_DASHBOARDS_INDEX_IDUSER on ts$users_dashboards ...
CREATE INDEX TS$USERS_DASHB_INDEX_IDUSER ON ts$users_dashboards
(
  ID_USER
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$users_dashboards TO PUBLIC;
PROMPT Creating Index TS$USERS_DASHBOARDS_INDEX_ROLE on ts$users_dashboards ...
CREATE INDEX TS$USERS_DASHBOARDS_INDEX_ROLE ON ts$users_dashboards
(
  ROLE
) 
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$users_dashboards TO PUBLIC;

PROMPT Creating Table ts$masters ...
CREATE TABLE ts$masters (
  ID NUMBER(10,0) NOT NULL,
  USERNAME VARCHAR2(200 CHAR) NOT NULL,
  CERTIFICATE_AUTHORITY VARCHAR2(200 CHAR),
  COLONIZER VARCHAR2(5 CHAR) DEFAULT 'false' NOT NULL,
  FULLNAME VARCHAR2(255 CHAR),
  EMAIL CLOB,
  REGISTER_DATE TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint ts$masters_PK on table ts$masters ... 
ALTER TABLE ts$masters
ADD CONSTRAINT ts$masters_PK PRIMARY KEY
(
  ID
)
ENABLE
;
PROMPT Creating Index TS$MASTERS_INDEX_NAME on ts$masters ...
CREATE INDEX TS$MASTERS_INDEX_USERNAME ON ts$masters (USERNAME);
CREATE INDEX TS$MASTERS_USERNAME_FT ON ts$masters (username) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$masters TO PUBLIC;

PROMPT Creating Table ts$roles ...
CREATE TABLE ts$roles (
  ID NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0),
  CODE VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(255 CHAR) NOT NULL,
  BEGIN_DATE TIMESTAMP(6) NOT NULL,
  EXPIRE_DATE TIMESTAMP(6),
  PARTNER_ID VARCHAR2(255 CHAR),
  PARTNER_SERVICE_NAME VARCHAR2(255 CHAR),
  CACHE VARCHAR2(255 CHAR)
);

PROMPT Creating Primary Key Constraint PRIMARY_5 on table ts$roles ... 
ALTER TABLE ts$roles ADD CONSTRAINT PRIMARY_5 PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index TS$ROLES_IDUSER on ts$roles ...
CREATE INDEX TS$ROLES_IDUSER ON ts$roles (ID_USER); 
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$roles TO PUBLIC;

PROMPT Creating Index TS$ROLES_CODE on ts$roles ...
CREATE INDEX TS$ROLES_CODE ON ts$roles (CODE); 
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$roles TO PUBLIC;

PROMPT Creating Index TS$ROLES_CACHE on ts$roles ...
CREATE INDEX TS$ROLES_CACHE ON ts$roles(cache) 
  INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');

PROMPT Creating Table ts$reports ...
CREATE TABLE ts$reports (
  ID VARCHAR2(255 CHAR) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL,
  TITLE VARCHAR2(255 CHAR) NOT NULL,
  DESCRIPTION VARCHAR2(1000 CHAR) NOT NULL,
  TEMPLATE VARCHAR2(255 CHAR) NOT NULL,
  DATA CLOB,
  CREATE_DATE TIMESTAMP(6) NOT NULL
);

CREATE INDEX ID_1 ON ts$reports(ID);
CREATE INDEX TS$REPORTS_IDUSER ON ts$reports (ID_USER);

PROMPT Creating Foreign Key Constraint ts$reports_idUser on table ts$users...
ALTER TABLE ts$reports ADD CONSTRAINT FK_ts$reports_idUser FOREIGN KEY (ID_USER) REFERENCES ts$users (ID) ENABLE;

PROMPT Creating Table ts$messagequeue ...
CREATE TABLE ts$messagequeue (
  ID NUMBER(10,0) NOT NULL,
  ORDER_ID NUMBER(10,0) NOT NULL,
  STATE NUMBER(10,0) NOT NULL,
  RETRIES NUMBER(10,0) NOT NULL,
  URI VARCHAR2(255 CHAR) NOT NULL,
  MESSAGE BLOB,
  CODE VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(255 CHAR) NOT NULL,
  HASH VARCHAR2(255 CHAR) NOT NULL,
  LAST_ERROR CLOB,
  CREATION_TIME TIMESTAMP(6) NOT NULL,
  LAST_UPDATE_TIME TIMESTAMP(6) NOT NULL
);

PROMPT Creating Primary Key Constraint ts$messagequeue_PK on table ts$messagequeue ...
ALTER TABLE ts$messagequeue ADD CONSTRAINT ts$messagequeue_PK PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index ts$messagequeue_state on ts$messagequeue ...
CREATE INDEX ts$messagequeue_state ON ts$messagequeue (state);

PROMPT Creating Table ts$events ...
CREATE TABLE ts$events (
  NAME VARCHAR2(255 CHAR) NOT NULL,
  DUE_DATE TIMESTAMP NOT NULL,
  DATA CLOB,
  FIRED VARCHAR(5 CHAR) DEFAULT '0'
);

PROMPT Creating Index TS$EVENTS_DUEDATE on ts$events ...
CREATE INDEX TS$EVENTS_DUEDATE ON ts$events(DUE_DATE);

PROMPT Creating Index TS$EVENTS_FIRED on ts$events ...
CREATE INDEX TS$EVENTS_FIRED ON ts$events(FIRED);

PROMPT Creating Foreign Key Constraint FK_ts$nodes_descriptors on table ts$nodes...
ALTER TABLE ts$nodes_descriptors
ADD CONSTRAINT FK_ts$nodes_references FOREIGN KEY
(
  ID_NODE
)
REFERENCES ts$nodes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Table ts$datastorequeue ...
CREATE TABLE ts$datastorequeue (
  ID NUMBER(10,0) NOT NULL,
  DATASTORE VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(30 CHAR) NOT NULL,
  CODE VARCHAR2(255 CHAR) NOT NULL,
  DATA CLOB,
  CREATE_DATE TIMESTAMP NOT NULL
);

ALTER TABLE ts$datastorequeue ADD CONSTRAINT PK_TS$DATASTOREQUEUE PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index TS$DATASTOREQUEUE_DATASTORE on ts$datastorequeue ...
CREATE INDEX TS$DATASTOREQUEUE_DATASTORE ON ts$datastorequeue(DATASTORE);


PROMPT Creating Foreign Key Constraint FK_ts$nodes_permissions on table ts$nodes...
ALTER TABLE ts$nodes_permissions
ADD CONSTRAINT FK_ts$nodes_permissions FOREIGN KEY
(
  ID_NODE
)
REFERENCES ts$nodes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$nodes_revisions on table ts$nodes...
ALTER TABLE ts$nodes_revisions
ADD CONSTRAINT FK_ts$nodes_revisions FOREIGN KEY
(
  ID_NODE
)
REFERENCES ts$nodes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$nodes_revisions on table ts$users...
ALTER TABLE ts$nodes_revisions
ADD CONSTRAINT FK_ts$nodes_revisions_iduser FOREIGN KEY
(
  ID_USER
)
REFERENCES ts$users
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$nodes_schemas on table ts$nodes...
ALTER TABLE ts$nodes_schemas
ADD CONSTRAINT FK_ts$nodes_schemas FOREIGN KEY
(
  ID_NODE
)
REFERENCES ts$nodes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$nodes_sd_revisions_idnod on table ts$nodes...
ALTER TABLE ts$nodes_superdata_revisions
ADD CONSTRAINT FK_ts$nodes_sd_revisions_idnod FOREIGN KEY
(
  ID_NODE
)
REFERENCES ts$nodes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$nods_sd_rvsns_idsperdata on table ts$nodes_superdata...
ALTER TABLE ts$nodes_superdata_revisions
ADD CONSTRAINT FK_ts$nods_sd_rvsns_idsperdata FOREIGN KEY
(
  ID_SUPER_DATA,
  ID_NODE
)
REFERENCES ts$nodes_superdata
(
  ID,
  ID_NODE
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$nodes_sd_rvisions_iduser on table ts$users...
ALTER TABLE ts$nodes_superdata_revisions
ADD CONSTRAINT FK_ts$nodes_sd_rvisions_iduser FOREIGN KEY
(
  ID_USER
)
REFERENCES ts$users
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$tasks_timers on table ts$tasks...
ALTER TABLE ts$tasks_timers
ADD CONSTRAINT FK_ts$tasks_timers FOREIGN KEY
(
  ID_TASK
)
REFERENCES ts$tasks
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$tasks_snapshots on table ts$tasks...
ALTER TABLE ts$tasks_snapshots
ADD CONSTRAINT FK_ts$tasks_snapshots FOREIGN KEY
(
  ID_TASK
)
REFERENCES ts$tasks
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$tasks_facts on table ts$tasks...
ALTER TABLE ts$tasks_facts
ADD CONSTRAINT FK_ts$tasks_facts FOREIGN KEY
(
  ID_TASK
)
REFERENCES ts$tasks
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$cubes_reports on table ts$cubes...
ALTER TABLE ts$cubes_reports
ADD CONSTRAINT FK_ts$cubes_reports FOREIGN KEY
(
  ID_CUBE
)
REFERENCES ts$cubes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$cubes_fg_idcube on table ts$cubes...
ALTER TABLE ts$cubes_filtergroups
ADD CONSTRAINT FK_ts$cubes_fg_idcube FOREIGN KEY
(
  ID_CUBE
)
REFERENCES ts$cubes
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$cubes_fg_iduser on table ts$users...
ALTER TABLE ts$cubes_filtergroups
ADD CONSTRAINT FK_ts$cubes_fg_iduser FOREIGN KEY
(
  ID_USER
)
REFERENCES ts$users
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint SIS_C008828 on table ts$users...
ALTER TABLE ts$users_nodes
ADD CONSTRAINT SIS_C008828 FOREIGN KEY
(
  ID_USER
)
REFERENCES ts$users
(
  ID
) ON DELETE CASCADE
ENABLE
;

PROMPT Creating Foreign Key Constraint FK_ts$mailboxes_TASK_ID on table ts$mailboxes...
ALTER TABLE ts$mailboxes ADD CONSTRAINT FK_ts$mailboxes_TASK_ID FOREIGN KEY (ID_TASK) REFERENCES ts$tasks (ID) ON DELETE CASCADE ENABLE;
PROMPT Creating Foreign Key Constraint FK_ts$mailboxes_permissions_ID on table ts$mailboxes...
ALTER TABLE ts$mailboxes_permissions ADD CONSTRAINT FK_ts$mailboxes_permi_ID FOREIGN KEY (ID) REFERENCES ts$mailboxes (ID) ON DELETE CASCADE ENABLE;

ALTER TABLE ts$locations ADD CONSTRAINT TS$LOCATIONS_TS$NODES_FK1 FOREIGN KEY (id_node)
	  REFERENCES ts$nodes (id) ON DELETE CASCADE ENABLE;

CREATE OR REPLACE TRIGGER ts$nodes_ID_TRG BEFORE INSERT OR UPDATE ON ts$nodes
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$nodes_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$nodes;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$nodes_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$nodes_superdata_ID_TRG BEFORE INSERT OR UPDATE ON ts$nodes_superdata
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$nodes_superdata_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$nodes_superdata;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$nodes_superdata_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$services_ID_TRG BEFORE INSERT OR UPDATE ON ts$services
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$services_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$services;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$services_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$sources_ID_TRG BEFORE INSERT OR UPDATE ON ts$sources
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$sources_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$sources;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$sources_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$reports_ID_TRG BEFORE INSERT OR UPDATE ON ts$reports
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$reports_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$reports;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$reports_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$nodes_permissions_ID_TRG BEFORE INSERT OR UPDATE ON ts$nodes_permissions
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$nodes_permissions_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$nodes_permissions;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$nodes_permissions_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_facts_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks_facts
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_facts_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks_facts;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_facts_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_snapshots_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks_snapshots
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_snapshots_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks_snapshots;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_snapshots_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$logbooknode_ID_TRG BEFORE INSERT OR UPDATE ON ts$logbooknode
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$logbooknode_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$logbooknode;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$logbooknode_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_timers_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks_timers
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_timers_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks_timers;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_timers_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$eventlog_ID_TRG BEFORE INSERT OR UPDATE ON ts$eventlog
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$eventlog_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$eventlog;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$eventlog_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$notifications_id_trg BEFORE INSERT OR UPDATE ON ts$notifications
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$notifications_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$notifications;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$notifications_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$workqueue_id_trg BEFORE INSERT OR UPDATE ON ts$workqueue
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$workqueue_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$workqueue;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$workqueue_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$locations_id_trg BEFORE INSERT OR UPDATE ON ts$locations
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$locations_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$locations;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$locations_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$cubes_ID_trg BEFORE INSERT OR UPDATE ON ts$cubes
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$cubes_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$cubes;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$cubes_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$cubes_reports_ID_trg BEFORE INSERT OR UPDATE ON ts$cubes_reports
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$cubes_reports_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$cubes_reports;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$cubes_reports_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$cubes_filtergroups_ID_trg BEFORE INSERT OR UPDATE ON ts$cubes_filtergroups
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$cubes_filtergroups_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$cubes_reports;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$cubes_filtergroups_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$posts_ID_trg BEFORE INSERT OR UPDATE ON ts$posts
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$posts_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$posts;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$posts_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$post_comments_ID_trg BEFORE INSERT OR UPDATE ON ts$post_comments
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$post_comments_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$post_comments;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$post_comments_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$nodes_revisions_ID_trg BEFORE INSERT OR UPDATE ON ts$nodes_revisions
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT ts$nodes_revisions_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$nodes_revisions;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$nodes_revisions_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$roles_ID_TRG BEFORE INSERT OR UPDATE ON ts$roles
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$roles_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$roles;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$roles_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$messagequeue_ID_TRG BEFORE INSERT OR UPDATE ON ts$messagequeue
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$messagequeue_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$messagequeue;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$messagequeue_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_orders_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks_orders
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_orders_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks_orders;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_orders_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_orders_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks_orders
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_orders_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks_orders;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_orders_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$tasks_orders_chats_ID_TRG BEFORE INSERT OR UPDATE ON ts$tasks_orders_chats_entries
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$tasks_orders_chats_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$tasks_orders_chats_entries;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$tasks_orders_chats_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$masters_ID_TRG BEFORE INSERT OR UPDATE ON ts$masters
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$masters_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$masters;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$masters_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ts$datastorequeue_ID_trg BEFORE INSERT OR UPDATE ON ts$datastorequeue
FOR EACH ROW
DECLARE
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  ts$datastorequeue_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM ts$datastorequeue;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ts$datastorequeue_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal;
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE FUNCTION group_concat (query_in in VARCHAR2) RETURN VARCHAR2 IS
    incoming    varchar2(4000);
    hold_result varchar2(4000);
    c sys_refcursor;
Begin
    open c for query_in;
    loop
        fetch c into incoming;
        exit when c%notfound;
        if incoming is not null then
          hold_result := hold_result||'. '||incoming;
        end if;
    end loop;
    return ltrim(hold_result,'. ');
END;

/
