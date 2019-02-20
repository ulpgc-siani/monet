CREATE SEQUENCE ds$templates_parts_SEQ
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;
  
CREATE SEQUENCE ds$templates_sign_fields$SEQ
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

CREATE SEQUENCE ds$templates_id_SEQ
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

CREATE SEQUENCE ds$work_queue_id_SEQ  
  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1  NOCYCLE ;

CREATE TABLE ds$info (
  name VARCHAR2 (255) NOT NULL,
  value VARCHAR2 (255) NOT NULL
);
INSERT INTO ds$info (name, value) VALUES ('version', '3.2.2');

CREATE TABLE ds$documents (
  id VARCHAR2(255 CHAR) NOT NULL,
  id_template NUMBER(24,0),
  state NUMBER(10,0),
  created_date DATE NOT NULL
);

ALTER TABLE ds$documents
  ADD CONSTRAINT ds$documents_PK PRIMARY KEY (id) ENABLE;

CREATE INDEX ds$documents_idtemplt ON ds$documents (id_template);
CREATE INDEX ds$documents_state ON ds$documents (state);

CREATE TABLE ds$documents_data (
  id_document VARCHAR2(255 CHAR) NOT NULL,
  content_type VARCHAR2(255 CHAR) NOT NULL,
  hash VARCHAR2(255 CHAR) NOT NULL,
  xml_data BLOB,
  location VARCHAR2(510 CHAR)
);

ALTER TABLE ds$documents_data
  ADD CONSTRAINT ds$documents_data_PK PRIMARY KEY (id_document) ENABLE;

CREATE TABLE ds$documents_preview_data (
  id_document VARCHAR2(255 CHAR) NOT NULL,
  page NUMBER(10,0) NOT NULL,
  data BLOB NOT NULL,
  content_type VARCHAR2(255 CHAR) NOT NULL,
  type NUMBER(3,0) NOT NULL,
  width NUMBER(10,0) NOT NULL,
  height NUMBER(10,0) NOT NULL,
  aspect_ratio FLOAT NOT NULL,
  created_date DATE NOT NULL
);

ALTER TABLE ds$documents_preview_data
  ADD CONSTRAINT ds$documents_preview_data_PK PRIMARY KEY (id_document,page,type) ENABLE;

CREATE INDEX ds$documents_preview_data_typ ON ds$documents_preview_data (type);

CREATE TABLE ds$documents_preview_type (
  id NUMBER(3,0) NOT NULL,
  label VARCHAR2(255 CHAR) NOT NULL
);

ALTER TABLE ds$documents_preview_type
  ADD CONSTRAINT ds$documents_preview_type_PK PRIMARY KEY (id) ENABLE;

CREATE TABLE ds$documents_states (
  id NUMBER(10,0) NOT NULL,
  label VARCHAR2(255 CHAR)
);

ALTER TABLE ds$documents_states
  ADD CONSTRAINT ds$documents_states_PK PRIMARY KEY (id) ENABLE;

CREATE TABLE ds$documents_type (
  id NUMBER(10,0) NOT NULL,
  label VARCHAR2(255 CHAR) NOT NULL
);

ALTER TABLE ds$documents_type
  ADD CONSTRAINT ds$documents_type_PK PRIMARY KEY (id) ENABLE;

CREATE TABLE ds$templates (
  code VARCHAR2(255 CHAR) NOT NULL,
  id NUMBER(24,0) NOT NULL,
  id_document_type NUMBER(10,0) NOT NULL,
  created_date DATE NOT NULL
);

ALTER TABLE ds$templates
  ADD CONSTRAINT ds$templates_PK PRIMARY KEY (id) ENABLE;

CREATE INDEX ds$templates_id_document_type ON ds$templates (id_document_type);

CREATE TABLE ds$templates_data (
  id_template NUMBER(24,0) NOT NULL,
  data BLOB,
  hash VARCHAR2(255 CHAR) NOT NULL,
  content_type VARCHAR2(255 CHAR) NOT NULL,
  signs_position VARCHAR2(255 CHAR)
);

CREATE INDEX ds$templates_data_id_template ON ds$templates_data (id_template);

CREATE TABLE ds$templates_parts (
  id_template NUMBER(24,0) NOT NULL,
  id VARCHAR2(255 CHAR) NOT NULL,
  data BLOB NOT NULL
);

ALTER TABLE ds$templates_parts
  ADD CONSTRAINT ds$templates_parts_PK PRIMARY KEY (id, id_template) ENABLE;

CREATE INDEX ds$templates_parts_id_templat ON ds$templates_parts (id_template);

CREATE TABLE ds$templates_sign_fields (
  id NUMBER(20) NOT NULL,
  id_template NUMBER(20) NOT NULL,
  sign_field varchar(255) NOT NULL
);

ALTER TABLE ds$templates_sign_fields
  ADD CONSTRAINT ds$templates_sign_fields$PK PRIMARY KEY (id, id_template) ENABLE;

CREATE INDEX ds$templates_signs_id_templat ON ds$templates_sign_fields (id_template);

CREATE TABLE ds$work_queue (
  id NUMBER(24,0) NOT NULL,
  id_document VARCHAR2(255 CHAR) NOT NULL,
  operation NUMBER(3,0) NOT NULL,
  queue_date TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  start_date TIMESTAMP,
  finish_date TIMESTAMP,
  state NUMBER(3,0) NOT NULL,
  error_msg CLOB,
  extra_data BLOB
);

ALTER TABLE ds$work_queue
  ADD CONSTRAINT ds$work_queue_PK PRIMARY KEY (id) ENABLE;

CREATE INDEX ds$work_queue_operation ON ds$work_queue (operation);
CREATE INDEX ds$work_queue_state ON ds$work_queue (state);
CREATE INDEX ds$work_queue_id_document ON ds$work_queue (id_document);

CREATE TABLE ds$work_queue_operations (
  id NUMBER(3,0) NOT NULL,
  label VARCHAR2(255 CHAR) NOT NULL
);

ALTER TABLE ds$work_queue_operations
  ADD CONSTRAINT ds$work_queue_operations_PK PRIMARY KEY (id) ENABLE;

CREATE TABLE ds$work_queue_states (
  id NUMBER(3,0) NOT NULL,
  label VARCHAR2(255 CHAR) NOT NULL
);

ALTER TABLE ds$work_queue_states
  ADD CONSTRAINT ds$work_queue_states_PK PRIMARY KEY (id) ENABLE;

ALTER TABLE ds$documents
  ADD CONSTRAINT ds$documents_templates_FK FOREIGN KEY (id_template)
    REFERENCES ds$templates (id) ENABLE;

ALTER TABLE ds$documents
  ADD CONSTRAINT ds$documents_docs_states_FK FOREIGN KEY (state)
    REFERENCES ds$documents_states (id) ENABLE;

ALTER TABLE ds$documents_data
  ADD CONSTRAINT ds$documents_data_docs_FK FOREIGN KEY (id_document)
    REFERENCES ds$documents (id) ON DELETE CASCADE ENABLE;

ALTER TABLE ds$documents_preview_data
  ADD CONSTRAINT ds$docs_pvw_dt_docs_pvw_tp_FK FOREIGN KEY (type)
    REFERENCES ds$documents_preview_type (id) ENABLE;

ALTER TABLE ds$documents_preview_data
  ADD CONSTRAINT ds$docs_preview_data_docs_FK FOREIGN KEY (id_document)
    REFERENCES ds$documents (id) ON DELETE CASCADE ENABLE;

ALTER TABLE ds$templates
  ADD CONSTRAINT ds$templates_documents_tp_FK FOREIGN KEY (id_document_type)
    REFERENCES ds$documents_type (id) ENABLE;

ALTER TABLE ds$templates_data
  ADD CONSTRAINT ds$templates_data_template_FK FOREIGN KEY (id_template)
    REFERENCES ds$templates (id) ON DELETE CASCADE ENABLE;

ALTER TABLE ds$templates_parts
  ADD CONSTRAINT ds$templates_parts_templat_FK FOREIGN KEY (id_template)
    REFERENCES ds$templates (id) ON DELETE CASCADE ENABLE;
	
ALTER TABLE ds$templates_sign_fields
  ADD CONSTRAINT ds$templates_signs_templat_FK FOREIGN KEY (id_template)
    REFERENCES ds$templates (id) ON DELETE CASCADE ENABLE;

ALTER TABLE ds$work_queue
  ADD CONSTRAINT ds$work_queue_work_queu_op_FK FOREIGN KEY (operation)
    REFERENCES ds$work_queue_operations (id) ENABLE;

ALTER TABLE ds$work_queue
  ADD CONSTRAINT ds$work_que_work_que_state_FK FOREIGN KEY (state)
    REFERENCES ds$work_queue_states (id) ENABLE;

CREATE OR REPLACE TRIGGER ds$templates_parts_TRG BEFORE INSERT OR UPDATE ON ds$templates_parts
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.id IS NULL THEN
    SELECT  ds$templates_parts_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(id),0) INTO v_newVal FROM ds$templates_parts;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ds$templates_parts_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.id := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ds$templates_sign_fields$TRG BEFORE INSERT OR UPDATE ON ds$templates_sign_fields
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.id IS NULL THEN
    SELECT  ds$templates_sign_fields$SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(id),0) INTO v_newVal FROM ds$templates_sign_fields;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ds$templates_sign_fields$SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.id := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ds$templates_id_TRG BEFORE INSERT OR UPDATE ON ds$templates
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.id IS NULL THEN
    SELECT  ds$templates_id_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(id),0) INTO v_newVal FROM ds$templates;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ds$templates_id_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.id := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE TRIGGER ds$work_queue_id_TRG BEFORE INSERT OR UPDATE ON ds$work_queue
FOR EACH ROW
DECLARE 
v_newVal NUMBER(12) := 0;
v_incval NUMBER(12) := 0;
BEGIN
  IF INSERTING AND :new.id IS NULL THEN
    SELECT  ds$work_queue_id_SEQ.NEXTVAL INTO v_newVal FROM DUAL;
    -- If this is the first time this table have been inserted into (sequence == 1)
    IF v_newVal = 1 THEN 
      --get the max indentity value from the table
      SELECT NVL(max(id),0) INTO v_newVal FROM ds$work_queue;
      v_newVal := v_newVal + 1;
      --set the sequence to that value
      LOOP
           EXIT WHEN v_incval>=v_newVal;
           SELECT ds$work_queue_id_SEQ.nextval INTO v_incval FROM dual;
      END LOOP;
    END IF;
    --used to emulate LAST_INSERT_ID()
    --mysql_utilities.identity := v_newVal; 
   -- assign the value from the sequence to emulate the identity column
   :new.id := v_newVal;
  END IF;
END;

/

CREATE OR REPLACE function to_milliseconds( i interval day to second )
  return number
is
begin
  return (extract( day from i )
       + extract( hour from i )/24
       + extract( minute from i )/24/60
       + extract( second from i )/24/60/60)*24*60*60;
end;

/
--
-- Volcar la base de datos para la tabla `documents_preview_type`
--

INSERT INTO ds$documents_preview_type VALUES(1, 'Thumbnail');
INSERT INTO ds$documents_preview_type VALUES(2, 'Page');

--
-- Volcar la base de datos para la tabla `documents_states`
--

INSERT INTO ds$documents_states VALUES(1, 'Editable');
INSERT INTO ds$documents_states VALUES(2, 'Consolidated');
INSERT INTO ds$documents_states VALUES(3, 'Locked');
INSERT INTO ds$documents_states VALUES(4, 'Processing');
INSERT INTO ds$documents_states VALUES(5, 'Error');
INSERT INTO ds$documents_states VALUES(6, 'Overwritten');

--
-- Volcar la base de datos para la tabla `documents_type`
--

INSERT INTO ds$documents_type VALUES(1, 'Open Document');
INSERT INTO ds$documents_type VALUES(2, 'Open Xml');
INSERT INTO ds$documents_type VALUES(3, 'Portable Document');
INSERT INTO ds$documents_type VALUES(4, 'Xml Document');

--
-- Volcar la base de datos para la tabla `work_queue_operations`
--

INSERT INTO ds$work_queue_operations VALUES(1, 'Update Document');
INSERT INTO ds$work_queue_operations VALUES(2, 'Consolidate Document');
INSERT INTO ds$work_queue_operations VALUES(3, 'Upload Preview Document');

--
-- Volcar la base de datos para la tabla `work_queue_states`
--

INSERT INTO ds$work_queue_states VALUES(0, 'Not started');
INSERT INTO ds$work_queue_states VALUES(1, 'Pending');
INSERT INTO ds$work_queue_states VALUES(2, 'In Progress');
INSERT INTO ds$work_queue_states VALUES(3, 'Finish');
INSERT INTO ds$work_queue_states VALUES(4, 'Error');


