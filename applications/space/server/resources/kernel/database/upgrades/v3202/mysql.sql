# Delete default value 'on update CURRENT_TIMESTAMP' of timestamp fields

DROP PROCEDURE IF EXISTS `changestructure_timestamp`;
CREATE PROCEDURE `changestructure_timestamp`()
BEGIN
  DECLARE hasMoreRows BOOL DEFAULT TRUE::SEMICOLON::
  DECLARE databasename TEXT::SEMICOLON::
  DECLARE tablename TEXT::SEMICOLON::
  DECLARE itemtablename VARCHAR(60)::SEMICOLON::
  DECLARE itemfieldname VARCHAR(60)::SEMICOLON::
  DECLARE ListTablesAndFieldsTimestamp CURSOR FOR SELECT `TABLES`.table_name, column_name FROM `INFORMATION_SCHEMA`.`TABLES`, `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLES`.`TABLE_SCHEMA` = databasename AND `TABLE_TYPE` = 'BASE TABLE' AND (`TABLES`.table_name LIKE 'tr$%' OR `TABLES`.table_name LIKE 'td$%' OR `TABLES`.table_name LIKE 'ts$%') AND `TABLES`.table_name = `COLUMNS`.table_name AND COLUMN_TYPE='timestamp' AND EXTRA = 'on update CURRENT_TIMESTAMP'::SEMICOLON::
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET hasMoreRows = FALSE::SEMICOLON::

  SELECT DATABASE() INTO databasename::SEMICOLON::
  SET FOREIGN_KEY_CHECKS=0::SEMICOLON::

  OPEN ListTablesAndFieldsTimestamp::SEMICOLON::
  FETCH ListTablesAndFieldsTimestamp INTO itemtablename, itemfieldname::SEMICOLON::
  WHILE hasMoreRows DO
      SET @s = CONCAT('ALTER TABLE `', itemtablename, '` CHANGE `', itemfieldname, '` `', itemfieldname ,'` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL')::SEMICOLON::
      PREPARE stmt FROM @s::SEMICOLON::
      EXECUTE stmt::SEMICOLON::     
    FETCH ListTablesAndFieldsTimestamp INTO itemtablename, itemfieldname::SEMICOLON::
  END WHILE::SEMICOLON::
  CLOSE ListTablesAndFieldsTimestamp::SEMICOLON::

  SET FOREIGN_KEY_CHECKS=1::SEMICOLON::
END;

CALL `changestructure_timestamp`();
DROP PROCEDURE `changestructure_timestamp`;
