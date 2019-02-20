PROMPT Adding DEFINITION_TYPE column on ts$nodes ...
ALTER TABLE ts$nodes ADD (DEFINITION_TYPE VARCHAR2(100) NOT NULL);

PROMPT Creating Index TS$NODES_DEFINITION_TYPE on ts$nodes ...
CREATE INDEX TS$NODES_DEFINITION_TYPE ON ts$nodes(DEFINITION_TYPE);

TRUNCATE TABLE ts$nodes_revisions;
ALTER TABLE ts$nodes_revisions ADD (id NUMBER(10,0) NOT NULL);
ALTER TABLE ts$nodes_revisions ADD CONSTRAINT PRIMARY_2 PRIMARY KEY (ID) ENABLE;

PROMPT Creating Sequence ts$roles_ID_SEQ ...
CREATE SEQUENCE  ts$roles_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

PROMPT Creating Table ts$roles ...
CREATE TABLE ts$roles (
  ID NUMBER(10,0) NOT NULL,
  ID_USER NUMBER(10,0) NOT NULL,
  CODE VARCHAR2(255 CHAR) NOT NULL,
  BEGIN_DATE TIMESTAMP(6) NOT NULL,
  EXPIRE_DATE TIMESTAMP(6)
);

PROMPT Creating Primary Key Constraint PRIMARY_5 on table ts$roles ... 
ALTER TABLE ts$roles ADD CONSTRAINT PRIMARY_5 PRIMARY KEY (ID) ENABLE;

PROMPT Creating Index TS$ROLES_IDUSER on ts$roles ...
CREATE INDEX TS$ROLES_IDUSER ON ts$roles (ID_USER); 
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$roles TO PUBLIC;

PROMPT Creating Index TS$ROLES_CODE on ts$roles ...
CREATE INDEX TS$ROLES_CODE ON ts$roles (CODE); 
GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES ON ts$roles TO PUBLIC;

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

CREATE INDEX TS$USERS_USERNAME_FT ON ts$users (username) INDEXTYPE IS CTXSYS.CONTEXT PARAMETERS ('LEXER monet_lexer SYNC ( ON COMMIT)');

Begin
  for c in (SELECT table_name FROM user_tables WHERE table_name LIKE 'TT$%') loop
    execute immediate ('ALTER TABLE '||c.table_name||' RENAME COLUMN data TO tags');
    execute immediate ('ALTER TABLE '||c.table_name||' ADD COLUMN is_new NUMBER(1,0) NOT NULL DEFAULT 0');
  end loop;
End;

/

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