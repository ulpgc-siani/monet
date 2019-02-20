DROP TABLE ts$tasks_facts;
DROP SEQUENCE ts$tasks_facts_ID_SEQ;

PROMPT Creating Sequence ts$tasks_facts_ID_SEQ ...
CREATE SEQUENCE  ts$tasks_facts_ID_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
PROMPT Creating Table ts$tasks_facts ...
CREATE TABLE ts$tasks_facts (
  ID NUMBER(10,0) NOT NULL,
  ID_TASK NUMBER(10,0) NOT NULL,
  ID_USER VARCHAR2(4000 CHAR) NOT NULL,
  TYPE VARCHAR2(255 CHAR) NOT NULL,
  WORKLINE_CODE VARCHAR2(6 CHAR),
  WORKSTOP_CODE VARCHAR2(6 CHAR),
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