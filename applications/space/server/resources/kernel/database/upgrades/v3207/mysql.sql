CREATE TABLE `ts$events` (
  `NAME` VARCHAR(255) NOT NULL,
  `DUE_DATE` TIMESTAMP NOT NULL,
  `DATA` LONGTEXT,
  `FIRED` VARCHAR(5) DEFAULT '0',
  PRIMARY KEY (`NAME`),
  KEY `TS$EVENTS_DUEDATE` (`DUE_DATE`),
  KEY `TS$EVENTS_FIRED` (`FIRED`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;