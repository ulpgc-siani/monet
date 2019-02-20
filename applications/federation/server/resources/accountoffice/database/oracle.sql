PROMPT Creating FullText Lexer Preference monet_lexer ...
begin
  ctx_ddl.create_preference('federation_lexer', 'BASIC_LEXER');
  ctx_ddl.set_attribute('federation_lexer', 'base_letter', 'YES');
end;

/

CREATE TABLE FS$FEDERATIONS (
  id NUMBER(10,0) NOT NULL ENABLE,
  name VARCHAR2(255 CHAR) NOT NULL ENABLE,
  label VARCHAR2(255 CHAR) NOT NULL ENABLE,
  uri CLOB,
  trusted NUMBER(10,0) DEFAULT 1,
  CONSTRAINT FS$FEDERATIONS_PK PRIMARY KEY (id) ENABLE
);

CREATE TABLE FS$FEDERATION_TRUST_REQUESTS (
  id_federation NUMBER(10,0) NOT NULL ENABLE,
  validation_code VARCHAR2(255 CHAR) NOT NULL ENABLE,
  create_date TIMESTAMP NOT NULL,
  CONSTRAINT FS$FEDERATIONS_TRUSTS_REQS_PK PRIMARY KEY (id_federation, validation_code) ENABLE
);

CREATE TABLE FS$BUSINESS_UNITS (
  id NUMBER(10,0) NOT NULL ENABLE,
  id_federation VARCHAR2(255 CHAR),
  name VARCHAR2(255 CHAR) NOT NULL ENABLE,
  label VARCHAR2(255 CHAR) NOT NULL ENABLE,
  type VARCHAR2(20 CHAR) NOT NULL ENABLE,
  uri VARCHAR2(2000 CHAR) NOT NULL ENABLE,
  secret VARCHAR2(255 CHAR) NOT NULL ENABLE,
  enable NUMBER(10,0) DEFAULT 1,
  visible NUMBER(10,0) DEFAULT 1,
  CONSTRAINT fs$business_units_PK PRIMARY KEY (id) ENABLE
);

CREATE TABLE FS$PARTNER_REQUESTS (
  id_business_unit NUMBER(10,0) NOT NULL ENABLE,
  validation_code VARCHAR2(255 CHAR) NOT NULL ENABLE,
  create_date TIMESTAMP NOT NULL,
  CONSTRAINT FS$PARTNER_REQUESTS_PK PRIMARY KEY (id_business_unit, validation_code) ENABLE
);

CREATE TABLE FS$SERVICES(
  id_business_unit VARCHAR2(255 CHAR) NOT NULL ENABLE,
  name VARCHAR2(100 CHAR) NOT NULL ENABLE,
  label VARCHAR2(255 CHAR) NOT NULL ENABLE,
  ontology VARCHAR2(255 CHAR) NOT NULL ENABLE,
  enable NUMBER(10,0) DEFAULT 1,
  CONSTRAINT FS$SERVICES_PK PRIMARY KEY (id_business_unit, name) ENABLE
);

CREATE TABLE FS$FEEDERS(
  id_business_unit VARCHAR2(255 CHAR) NOT NULL ENABLE,
  name VARCHAR2(100 CHAR) NOT NULL ENABLE,
  label VARCHAR2(255 CHAR) NOT NULL ENABLE,
  ontology VARCHAR2(255 CHAR) NOT NULL ENABLE,
  enable NUMBER(10,0) DEFAULT 1,
  CONSTRAINT FS$FEEDERS_PK PRIMARY KEY (id_business_unit, name) ENABLE
);

CREATE TABLE FS$USERS (
	id NUMBER(10,0) NOT NULL ENABLE,
	username VARCHAR2(255 CHAR),
  fullname CLOB,
	email VARCHAR2(255 CHAR),
	lang VARCHAR2(2 CHAR),
	"mode" VARCHAR2(100 CHAR),
	human NUMBER(10,0) DEFAULT 0,
	CONSTRAINT FS$USERS_PK PRIMARY KEY (id) ENABLE,
	CONSTRAINT FS$USERS_ID_USER UNIQUE (username) ENABLE,
	CONSTRAINT FS$USERS_EMAIL UNIQUE (email) ENABLE
);
CREATE INDEX FS$USERS_USERNAME ON fs$users (username) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER federation_lexer SYNC ( ON COMMIT)');
CREATE INDEX FS$USERS_FULLNAME ON fs$users (fullname) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER federation_lexer SYNC ( ON COMMIT)');
  
CREATE TABLE FS$AUTHDB_USERS(
	id NUMBER(10,0) NOT NULL ENABLE,
	id_user VARCHAR2(2000 CHAR) NOT NULL ENABLE,
	password VARCHAR2(255 CHAR),
	CONSTRAINT FS$AUTHDB_USERS_PK PRIMARY KEY (id) ENABLE,
	CONSTRAINT FS$AUTHDB_USERS_ID_USER UNIQUE (id_user) ENABLE
);

CREATE TABLE FS$LOGIN_ATTEMPTS (
  ip VARCHAR2(255 BYTE) NOT NULL ENABLE,
  attempts NUMBER(10,0) NOT NULL ENABLE,
  last_attempt NUMBER(10,0),
  CONSTRAINT FS$IP_PK PRIMARY KEY (ip) ENABLE
);

CREATE TABLE FS$TOKENS (
  token VARCHAR2(255 BYTE) NOT NULL ENABLE,
  username VARCHAR2(255 BYTE) NOT NULL ENABLE,
  remember_me NUMBER(10,0) DEFAULT 0,
  verifier VARCHAR2(255 BYTE) NOT NULL ENABLE,
  lang VARCHAR2(2 CHAR),
  is_mobile NUMBER(10,0) DEFAULT 0,
  last_use TIMESTAMP NOT NULL,
  space VARCHAR2(255 BYTE),
  node VARCHAR2(255 BYTE),
  CONSTRAINT FS$TOKEN_PK PRIMARY KEY (token) ENABLE
);

CREATE TABLE FS$MOBILE_DEVICES (
  ID VARCHAR2(255) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL
);

ALTER TABLE FS$MOBILE_DEVICES
ADD CONSTRAINT TS$USERS_MOBILE_DEVICES_PK PRIMARY KEY
(
  ID,
  ID_USER
)
ENABLE
;
CREATE INDEX FS$MOBILE_DEVICES_INDEX_IDUSER ON FS$MOBILE_DEVICES
(
  ID_USER
)
;
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON FS$MOBILE_DEVICES TO PUBLIC;

CREATE SEQUENCE FS$FEDERATION_ID_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE OR REPLACE TRIGGER FS$FEDERATION_ID_TRG BEFORE INSERT OR UPDATE ON FS$FEDERATIONS
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  FS$FEDERATION_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM FS$FEDERATIONS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT FS$FEDERATION_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;
/
ALTER TRIGGER FS$FEDERATION_ID_TRG ENABLE;

CREATE SEQUENCE FS$BUSINESSUNIT_ID_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE OR REPLACE TRIGGER FS$BUSINESSUNIT_ID_TRG BEFORE INSERT OR UPDATE ON FS$BUSINESS_UNITS
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  FS$BUSINESSUNIT_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM FS$BUSINESS_UNITS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT FS$BUSINESSUNIT_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;
/
ALTER TRIGGER FS$BUSINESSUNIT_ID_TRG ENABLE;

CREATE SEQUENCE FS$USER_ID_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE OR REPLACE TRIGGER FS$USER_ID_TRG BEFORE INSERT OR UPDATE ON FS$USERS
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  FS$USER_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM FS$USERS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT FS$USER_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;
/
ALTER TRIGGER FS$USER_ID_TRG ENABLE;

CREATE SEQUENCE FS$AUTHDB_USER_ID_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
CREATE OR REPLACE TRIGGER FS$AUTHDB_USER_ID_TRG BEFORE INSERT OR UPDATE ON FS$AUTHDB_USERS
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.ID IS NULL THEN
    SELECT  FS$AUTHDB_USER_ID_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(ID),0) INTO v_newVal FROM FS$AUTHDB_USERS;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT FS$AUTHDB_USER_ID_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.ID := v_newVal;
  END IF;
END;
/
ALTER TRIGGER FS$AUTHDB_USER_ID_TRG ENABLE;

commit;
