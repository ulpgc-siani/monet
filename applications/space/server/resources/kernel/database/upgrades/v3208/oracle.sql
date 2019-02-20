CREATE SEQUENCE  ts$datastorequeue_ID_SEQ
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

CREATE TABLE ts$datastorequeue (
  ID NUMBER(10,0) NOT NULL,
  DATASTORE VARCHAR2(255 CHAR) NOT NULL,
  TYPE VARCHAR2(30 CHAR) NOT NULL,
  CODE VARCHAR2(255 CHAR) NOT NULL,
  DATA CLOB,
  CREATE_DATE TIMESTAMP NOT NULL
);

ALTER TABLE ts$datastorequeue ADD CONSTRAINT PK_TS$DATASTOREQUEUE PRIMARY KEY (ID) ENABLE;
CREATE INDEX TS$DATASTOREQUEUE_DATASTORE ON ts$datastorequeue(DATASTORE);

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
