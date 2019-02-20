DELIMITER $$

DROP PROCEDURE IF EXISTS `emptydatabase`$$
CREATE PROCEDURE `emptydatabase`()
BEGIN
  DECLARE done INT;
  DECLARE databasename TEXT;
  DECLARE itemname VARCHAR(60);
  DECLARE sqlcommand VARCHAR(255);
  DECLARE ListViews CURSOR FOR SELECT `TABLE_NAME` FROM `INFORMATION_SCHEMA`.`TABLES` WHERE `TABLE_SCHEMA` = databasename AND `TABLE_TYPE` = 'VIEW' AND table_name LIKE 'ds$%';
  DECLARE ListTables CURSOR FOR SELECT `TABLE_NAME` FROM `INFORMATION_SCHEMA`.`TABLES` WHERE `TABLE_SCHEMA` = databasename AND `TABLE_TYPE` = 'BASE TABLE' AND table_name LIKE 'ds$%';
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;    

  SELECT DATABASE() INTO databasename;
  SET FOREIGN_KEY_CHECKS=0;

  SET done = 0;
  OPEN ListViews;
  REPEAT
    FETCH ListViews INTO itemname;
    IF NOT done THEN
      SET @s = CONCAT('DROP VIEW ', itemname);
      PREPARE stmt FROM @s;
      EXECUTE stmt;     
    END IF;
  UNTIL done END REPEAT;
  CLOSE ListViews;
  
  SET done = 0;
  OPEN ListTables;
  REPEAT
    FETCH ListTables INTO itemname;
    IF NOT done THEN
      SET @s = CONCAT('DROP TABLE ', itemname);
      PREPARE stmt FROM @s;
      EXECUTE stmt;     
    END IF;
  UNTIL done END REPEAT;
  CLOSE ListTables;

  SET FOREIGN_KEY_CHECKS=1;
END$$



CALL `emptydatabase`()$$
DROP PROCEDURE `emptydatabase`$$

