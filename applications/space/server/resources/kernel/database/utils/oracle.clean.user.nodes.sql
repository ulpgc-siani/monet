--This script drops all reference tables, all nodes, all tasks and all back users
-- ¡¡USE WITH CAUTION!!

Begin
  for c in (SELECT table_name FROM user_tables WHERE table_name LIKE 'TT$%') loop
    execute immediate ('drop table '||c.table_name||' cascade constraints');
  end loop;
  for c in (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'TT$%') loop
    execute immediate ('drop sequence '||c.sequence_name);
  end loop;
  
  DELETE FROM TS$NODES;
  DELETE FROM TS$TASKS;
  DELETE FROM TS$USERS WHERE TYPE='back';
End;