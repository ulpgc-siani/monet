CREATE TABLE `ts$datastorequeue` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `DATASTORE` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(30) NOT NULL,
  `CODE` VARCHAR(255) NOT NULL,
  `DATA` LONGTEXT,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$EVENTS_DATASTORE` (`DATASTORE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
