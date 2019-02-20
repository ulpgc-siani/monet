begin
  for c in (SELECT * FROM user_indexes WHERE uniqueness='NONUNIQUE' AND (index_name LIKE 'FS$%')) loop
    execute immediate ('drop index '||c.index_name);
  end loop;
  for c in (SELECT table_name FROM user_tables WHERE table_name LIKE 'FS$%') loop
    execute immediate ('drop table '||c.table_name||' cascade constraints');
  end loop;
  for c in (SELECT table_name FROM user_tables WHERE table_name LIKE 'APEX$%' OR table_name LIKE 'DEMO_%' OR table_name LIKE 'DEPT' OR table_name LIKE 'EMP') loop
    execute immediate ('drop table '||c.table_name||' cascade constraints');
  end loop; 
  for c in (SELECT view_name FROM user_views WHERE view_name LIKE 'FS$%') loop
    execute immediate ('drop view '||c.view_name);
  end loop;
  for c in (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'FS$%') loop
    execute immediate ('drop sequence '||c.sequence_name);
  end loop;
  for c in (SELECT synonym_name FROM user_synonyms WHERE synonym_name LIKE 'FS$%') loop
    execute immediate ('drop synonym '||c.synonym_name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='PROCEDURE' AND name LIKE 'FS$%') loop
    execute immediate ('drop procedure '||c.name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='FUNCTION' AND name LIKE 'FS$%') loop
    execute immediate ('drop function '||c.name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='PACKAGE' AND name LIKE 'FS$%') loop
    execute immediate ('drop package '||c.name);
  end loop;
  for c in (SELECT distinct name FROM user_source WHERE TYPE='TRIGGER' AND name LIKE 'FS$%') loop
    execute immediate ('drop trigger "'||c.name||'"');
  end loop;  
end;
/

begin
  ctx_ddl.drop_preference('federation_lexer');
end;
/
