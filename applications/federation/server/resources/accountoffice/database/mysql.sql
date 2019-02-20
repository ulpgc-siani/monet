DROP TABLE IF EXISTS `fs$federations`;
CREATE TABLE `fs$federations` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `label` VARCHAR(255) NOT NULL,
  `uri` TEXT NOT NULL,
  `trusted` INT(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$federation_trust_requests`;
CREATE TABLE `fs$federation_trust_requests` (
  `id_federation` BIGINT(20) NOT NULL,
  `validation_code` VARCHAR(255) NOT NULL,
  `create_date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_federation`,`validation_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$business_units`;
CREATE TABLE `fs$business_units` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_federation` VARCHAR(255),
  `name` VARCHAR(255) NOT NULL,
  `label` VARCHAR(255) NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  `uri` TEXT NOT NULL,
  `secret` VARCHAR(255) NOT NULL,
  `enable` INT(1) NOT NULL DEFAULT 1,
  `visible` INT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$partner_requests`;
CREATE TABLE `fs$partner_requests` (
  `id_business_unit` BIGINT(20) NOT NULL,
  `validation_code` VARCHAR(255) NOT NULL,
  `create_date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_business_unit`,`validation_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$services`;
CREATE TABLE `fs$services` (
  `id_business_unit` VARCHAR(70) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `label` TEXT NOT NULL,
  `ontology` VARCHAR(255) NOT NULL,
  `enable` INT(1) NOT NULL,
  PRIMARY KEY (`id_business_unit`, `name`)
) DEFAULT CHARSET=utf8;
  
DROP TABLE IF EXISTS `fs$feeders`;
CREATE TABLE `fs$feeders` (
  `id_business_unit` VARCHAR(70) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `label` TEXT NOT NULL,
  `ontology` VARCHAR(255) NOT NULL,
  `enable` INT(1) NOT NULL,
  PRIMARY KEY (`id_business_unit`, `name`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$users`;
CREATE TABLE `fs$users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255),
  `fullname` TEXT,
  `email` VARCHAR(255),
  `lang` VARCHAR(2),
  `mode` VARCHAR(100),
  `human` INT(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  CONSTRAINT UNIQUE (`username`),
  CONSTRAINT UNIQUE (`email`)
) ENGINE=MYISAM DEFAULT CHARSET=utf8;
ALTER TABLE fs$users ADD FULLTEXT fs$users_username (`username`);
ALTER TABLE fs$users ADD FULLTEXT fs$users_fullname (`fullname`);

DROP TABLE IF EXISTS `fs$authdb_users`;
CREATE TABLE `fs$authdb_users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_user` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255),
  PRIMARY KEY (`id`),
  CONSTRAINT UNIQUE (`id_user`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$login_attempts`;
CREATE TABLE `fs$login_attempts` (
  `ip` VARCHAR(255) NOT NULL,
  `attempts` INT(11) NOT NULL DEFAULT '0',
  `last_attempt` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`ip`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$tokens`;
CREATE TABLE `fs$tokens` (
  `token` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `remember_me` INT(1) NOT NULL DEFAULT '0',
  `verifier` VARCHAR(255) NOT NULL,
  `lang` VARCHAR(2),
  `is_mobile` INT(11) NOT NULL DEFAULT '0',
  `last_use` TIMESTAMP NOT NULL,
  `space` varchar(255) DEFAULT NULL,
  `node` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`token`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fs$mobile_devices`;
CREATE TABLE `fs$mobile_devices` (
  `ID` VARCHAR(255) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  PRIMARY KEY (`ID`,`ID_USER`),
  KEY `FS$MOBILE_DEVICES_INDEX_IDUSER` (`ID_USER`)
) DEFAULT CHARSET=utf8;

INSERT INTO `fs$business_units`(`id`,`id_federation`,`name`,`label`,`type`,`uri`,`secret`,`enable`) VALUES ( NULL,NULL,'notouch','-','member','http://localhost:8080/nottouch','1234','0'); 
