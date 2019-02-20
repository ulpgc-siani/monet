Begin
  ctx_ddl.drop_preference('monet_lexer');
End;

/

Begin
  for c in (SELECT table_name FROM user_tables WHERE table_name LIKE 'TR$%' OR table_name LIKE 'TD$%' OR table_name LIKE 'TS$%' OR table_name LIKE 'TDI$%' OR table_name LIKE 'TDS$%' OR table_name LIKE 'TDU$%') loop
    execute immediate ('drop table '||c.table_name||' cascade constraints');
  end loop;
  for c in (SELECT table_name FROM user_tables WHERE table_name LIKE 'APEX$%' OR table_name LIKE 'DEMO_%' OR table_name LIKE 'DEPT' OR table_name LIKE 'EMP') loop
    execute immediate ('drop table '||c.table_name||' cascade constraints');
  end loop;   
  for c in (SELECT view_name FROM user_views WHERE view_name LIKE 'TR$%' OR view_name LIKE 'TD$%') loop
    execute immediate ('drop view '||c.view_name);
  end loop;
  for c in (SELECT * FROM user_indexes WHERE uniqueness='NONUNIQUE' AND (index_name LIKE 'TR$%' OR index_name LIKE 'TD$%' OR index_name LIKE 'TS$%')) loop
    execute immediate ('drop index '||c.index_name);
  end loop;
  for c in (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'TS$%' OR sequence_name LIKE 'TD$%' OR sequence_name LIKE 'TDI$%') loop
    execute immediate ('drop sequence '||c.sequence_name);
  end loop;
  for c in (SELECT synonym_name FROM user_synonyms WHERE synonym_name LIKE 'TS$%') loop
    execute immediate ('drop synonym '||c.synonym_name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='PROCEDURE' AND name LIKE 'TS$%') loop
    execute immediate ('drop procedure '||c.name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='FUNCTION' AND name LIKE 'TS$%') loop
    execute immediate ('drop function '||c.name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='PACKAGE' AND name LIKE 'TS$%') loop
    execute immediate ('drop package '||c.name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='TRIGGER' AND name LIKE 'TS$%' OR name LIKE 'BIN$%') loop
    execute immediate ('drop trigger "'||c.name||'"');
  end loop;
  for c in (SELECT distinct table_name FROM USER_SDO_GEOM_METADATA WHERE table_name LIKE 'TS$%') loop
    execute immediate ('DELETE FROM USER_SDO_GEOM_METADATA WHERE table_name= '''||c.table_name||'''');
  end loop;  
End;

/
