/*
SQLyog Community- MySQL GUI v8.22 
MySQL - 5.1.37 : Database - monet_skeleton
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `ts$default_values` */

CREATE TABLE `ts$default_values` (
  `CODE_NODE` VARCHAR(100) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `PROPERTY` VARCHAR(100) NOT NULL,
  `DATA` LONGTEXT,
  KEY `CODE_NODE` (`CODE_NODE`,`ID_USER`),
  KEY `CODE_NODE_2` (`CODE_NODE`,`ID_USER`,`PROPERTY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$default_values` */

/*Table structure for table `ts$post_types` */
CREATE TABLE ts$post_types (
  id INT(11) NOT NULL,
  label VARCHAR(255),
  PRIMARY KEY(id)
);

/*Data for the table `ts$post_types` */

INSERT INTO ts$post_types (id, label) VALUES (0, 'ServiceRequestReceived');
INSERT INTO ts$post_types (id, label) VALUES (1, 'ServiceResponseSent');
INSERT INTO ts$post_types (id, label) VALUES (2, 'ServiceProviderRequest');
INSERT INTO ts$post_types (id, label) VALUES (3, 'ServiceProviderResponse');
INSERT INTO ts$post_types (id, label) VALUES (4, 'ServiceProviderNotifySent');
INSERT INTO ts$post_types (id, label) VALUES (5, 'ServiceProviderNotifyReceived');
INSERT INTO ts$post_types (id, label) VALUES (6, 'BusinessModelUpdated');
INSERT INTO ts$post_types (id, label) VALUES (7, 'TaskEvent');
INSERT INTO ts$post_types (id, label) VALUES (8, 'NewNode');
INSERT INTO ts$post_types (id, label) VALUES (9, 'UserPost');
INSERT INTO ts$post_types (id, label) VALUES (10, 'ServiceProviderAlert');
INSERT INTO ts$post_types (id, label) VALUES (11, 'TaskAlertUser');
INSERT INTO ts$post_types (id, label) VALUES (12, 'NodeAlertUser');

/*Table structure for table `ts$posts` */
CREATE TABLE ts$posts (
  id INT(11) NOT NULL AUTO_INCREMENT,
  create_date TIMESTAMP NOT NULL,
  type INT(11) NOT NULL,
  title VARCHAR(512),
  body TEXT,
  body_target INT(11),
  wall_user_id INT(11),
  target INT(11),
  target_code VARCHAR(100),
  tag VARCHAR(100),
  PRIMARY KEY(id),
  KEY `IDX_ts$posts_create_date` (`create_date`),
  CONSTRAINT FK_ts$posts_type FOREIGN KEY (`type`) REFERENCES ts$post_types (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Data for the table `ts$posts` */

/*Table structure for table `ts$post_comments` */
CREATE TABLE ts$post_comments (
  id INT(11) NOT NULL AUTO_INCREMENT,
  create_date TIMESTAMP NOT NULL,
  id_author INT(11) NOT NULL,
  author VARCHAR(255),
  text TEXT,
  id_post INT(11) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT FK_ts$post_comments_posts FOREIGN KEY (`id_post`) REFERENCES ts$posts (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Data for the table `ts$post_comments` */

/*Table structure for table `ts$post_user_filter` */
CREATE TABLE ts$post_user_filter (
  id_user INT(11) NOT NULL,
  id_post INT(11) NOT NULL,
  PRIMARY KEY (`id_user`, `id_post`),
  CONSTRAINT FK_ts$post_user_filter FOREIGN KEY (`id_post`) REFERENCES ts$posts (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `ts$post_user_author_filter` */
CREATE TABLE ts$post_user_author_filter (
  id_user INT(11) NOT NULL,
  id_author INT(11) NOT NULL,
  PRIMARY KEY (`id_user`, `id_author`)
);

/*Table structure for table `ts$locations` */
CREATE TABLE ts$locations (
  id INT(11) NOT NULL AUTO_INCREMENT,
  id_node INT(11) NOT NULL,
  id_location VARCHAR(255),
  field_code VARCHAR(100),
  metadata TEXT,
  `geometry` GEOMETRY NOT NULL,
  create_date TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_ts$locations_ts$nodes FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*CREATE SPATIAL INDEX ts$locations_geometry ON ts$locations (`geometry`);*/

CREATE TABLE `ts$locations_geometry` SELECT `ID`,`geometry` FROM `ts$locations`;
ALTER TABLE `ts$locations_geometry` ENGINE=MyISAM;
CREATE SPATIAL INDEX ts$locations_geometry ON ts$locations_geometry (`geometry`);

DELIMITER $$

CREATE
    TRIGGER `tr_ts$locations_insert` AFTER INSERT
    ON `ts$nodes_descriptors`
    FOR EACH ROW BEGIN
        INSERT INTO `ts$nodes_descriptors_search` (`ID_NODE`,`LABEL`,`DESCRIPTION`) VALUES (new.`ID_NODE`,new.`LABEL`,new.`DESCRIPTION`);
END$$

CREATE
    TRIGGER `tr_ts$nodes_descriptors_update` AFTER UPDATE
    ON `ts$nodes_descriptors`
    FOR EACH ROW BEGIN
        UPDATE `ts$nodes_descriptors_search` SET `LABEL`=new.`LABEL`,`DESCRIPTION`=new.`DESCRIPTION` WHERE `ID_NODE` = new.`ID_NODE`;
END$$

CREATE
    TRIGGER `tr_ts$nodes_descriptors_delete` AFTER DELETE
    ON `ts$nodes_descriptors`
    FOR EACH ROW BEGIN
        DELETE FROM `ts$nodes_descriptors_search` WHERE `ID_NODE` = old.`ID_NODE`;
END$$

DELIMITER ;









/*Data for the table `ts$locations` */


/*Table structure for table `ts$notifications` */
CREATE TABLE ts$workqueue (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` INT(11) NOT NULL,
  `state` INT(11) NOT NULL,
  `retries` INT(11) NOT NULL,
  `target` LONGTEXT NOT NULL,
  `last_error` TEXT,
  `creation_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Data for the table `ts$workqueue` */

/*Table structure for table `ts$notifications` */
CREATE TABLE `ts$notifications` ( 
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT, 
  `id_user` INT(11) NOT NULL, 
  `label` TEXT NOT NULL, 
  `icon` TEXT, 
  `read` INT(1) NOT NULL, 
  `create_date` TIMESTAMP NOT NULL, 
  `target` VARCHAR(255), 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Data for the table `ts$notifications` */

/*Table structure for table `ts$eventlog` */

CREATE TABLE `ts$eventlog` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `logger` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `priority` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `message` LONGTEXT COLLATE utf8_unicode_ci NOT NULL,
  `stacktrace` TEXT COLLATE utf8_unicode_ci,
  `creation_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  KEY `priority` (`priority`),
  KEY `creationTime` (`creation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$eventlog` */

/*Table structure for table `ts$logbooknode` */


CREATE TABLE `ts$logbooknode` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `HOST` TEXT NOT NULL,
  `LAYER` TEXT NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `ID_NODE` INT(11) NOT NULL,
  `VISITED` INT(1) NOT NULL,
  `CREATED` INT(1) NOT NULL,
  `DELETED` INT(1) NOT NULL,
  `MODIFIED` INT(1) NOT NULL,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$logbooknode` */

/*Table structure for table `ts$logbooknode_subscribers` */

CREATE TABLE `ts$logbooknode_subscribers` (
  `SERVER_NAME` TEXT NOT NULL,
  `SERVER_HOST` TEXT NOT NULL,
  `SERVER_PORT` INT(1) NOT NULL,
  `TYPE` INT(1) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `REGISTER_DATE` TIMESTAMP NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$logbooknode_subscribers` */

/*Table structure for table `ts$nodes` */

CREATE TABLE `ts$nodes` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_PARENT` INT(11),
  `ID_OWNER` INT(11) DEFAULT NULL,
  `ID_PROTOTYPE` int(11) DEFAULT NULL,
  `CODE` VARCHAR(100) NOT NULL,
  `ORDERING` INT(1) NOT NULL,
  `DATA` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `NOTES` LONGTEXT,
  `LOCKED` INT(1) DEFAULT '0',
  `VALID` INT(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `TS$NODES_CODE` (`CODE`(64)),
  KEY `TS$NODES_IDOWNER` (`ID_OWNER`),
  KEY `TS$NODES_IDPARENT` (`ID_PARENT`),
  KEY `TS$NODES_IDPROTOTYPE` (`ID_PROTOTYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/* FULLTEXT(`DATA`)*/

CREATE TABLE `ts$nodes_search` SELECT `ID`,`DATA` FROM `ts$nodes`;
ALTER TABLE `ts$nodes_search` ENGINE=MyISAM;
ALTER TABLE `ts$nodes_search` ADD FULLTEXT (`DATA`);

DELIMITER $$

CREATE
    TRIGGER `tr_ts$nodes_insert` AFTER INSERT
    ON `ts$nodes`
    FOR EACH ROW BEGIN
        INSERT INTO `ts$nodes_search`(`ID`,`DATA`) VALUES (new.`ID`,new.`DATA`);
END$$

CREATE
    TRIGGER `tr_ts$nodes_update` AFTER UPDATE
    ON `ts$nodes`
    FOR EACH ROW BEGIN
        UPDATE `ts$nodes_search` SET `DATA`=new.`DATA` WHERE `ID` = new.`ID`;
END$$

CREATE
    TRIGGER `tr_ts$nodes_delete` AFTER DELETE
    ON `ts$nodes`
    FOR EACH ROW BEGIN
        DELETE FROM `ts$nodes_search`  WHERE `ID` = old.`ID`;
END$$

DELIMITER ;

/*Data for the table `ts$nodes` */

/*Table structure for table `ts$nodes_ancestors` */

CREATE TABLE `ts$nodes_ancestors` (
  `ID_ANCESTOR` INT(11) NOT NULL,
  `ID_NODE` INT(11) NOT NULL,
  `ANCESTOR_LEVEL` INT(11) NOT NULL,
  CONSTRAINT `FK_ts$nodes_ancestors_id_ancestor` FOREIGN KEY (`ID_ANCESTOR`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ts$nodes_ancestors_id_node` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$nodes_ancestors` */

/*Table structure for table `ts$nodes_descriptors` */


CREATE TABLE `ts$nodes_descriptors` (
  `ID_NODE` INT(11) DEFAULT NULL,
  `ID_OWNER` INT(11) DEFAULT NULL,
  `ID_PARENT` INT(11) DEFAULT NULL,
  `CODE` TEXT,
  `LABEL` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `DESCRIPTION` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `ORDERING` INT(11) DEFAULT NULL,
  `CREATE_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  `DELETE_DATE` TIMESTAMP NULL,
  `HIGHLIGHTED` VARCHAR(5) DEFAULT NULL,
  `EDITABLE` VARCHAR(5) DEFAULT NULL,
  `PROTOTYPE` int(1) DEFAULT 0,
  KEY `TS$NODES_DESCRIPTORS_CODE` (`CODE`(64)),
  KEY `TS$NODES_DESCRIPTORS_CREATEDATE` (`CREATE_DATE`),
  KEY `TS$NODES_DESCRIPTORS_DELETEDATE` (`DELETE_DATE`),
  KEY `TS$NODES_DESCRIPTORS_HIGHTED` (`HIGHLIGHTED`),
  KEY `TS$NODES_DESCRIPTORS_IDNODE` (`ID_NODE`),
  KEY `TS$NODES_DESCRIPTORS_IDOWNER` (`ID_OWNER`),
  KEY `TS$NODES_DESCRIPTORS_LABEL` (`LABEL`(64)),
  KEY `TS$NODES_DESCRIPTORS_ORDERING` (`DESCRIPTION`(64)),
  KEY `TS$NODES_DESCRIPTORS_UPDATEDATE` (`UPDATE_DATE`),
  KEY `TS$NODES_DESCRIPTORS_IDPARENT` (`ID_PARENT`),
  KEY `TS$NODES_DESCRIPTORS_PROTOTYPE` (`PROTOTYPE`),
  CONSTRAINT `FK_ts$nodes_descriptors` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*  FULLTEXT(`LABEL`, `DESCRIPTION`) */
CREATE TABLE `ts$nodes_descriptors_search` SELECT `ID_NODE`,`LABEL`,`DESCRIPTION` FROM `ts$nodes_descriptors`;
ALTER TABLE `ts$nodes_descriptors_search` ENGINE=MyISAM;
ALTER TABLE `ts$nodes_descriptors_search` ADD FULLTEXT (`LABEL`, `DESCRIPTION`);

DELIMITER $$

CREATE
    TRIGGER `tr_ts$nodes_descriptors_insert` AFTER INSERT
    ON `ts$nodes_descriptors`
    FOR EACH ROW BEGIN
        INSERT INTO `ts$nodes_descriptors_search` (`ID_NODE`,`LABEL`,`DESCRIPTION`) VALUES (new.`ID_NODE`,new.`LABEL`,new.`DESCRIPTION`);
END$$

CREATE
    TRIGGER `tr_ts$nodes_descriptors_update` AFTER UPDATE
    ON `ts$nodes_descriptors`
    FOR EACH ROW BEGIN
        UPDATE `ts$nodes_descriptors_search` SET `LABEL`=new.`LABEL`,`DESCRIPTION`=new.`DESCRIPTION` WHERE `ID_NODE` = new.`ID_NODE`;
END$$

CREATE
    TRIGGER `tr_ts$nodes_descriptors_delete` AFTER DELETE
    ON `ts$nodes_descriptors`
    FOR EACH ROW BEGIN
        DELETE FROM `ts$nodes_descriptors_search` WHERE `ID_NODE` = old.`ID_NODE`;
END$$

DELIMITER ;

/*Data for the table `ts$nodes_descriptors` */

/*Table structure for table `ts$nodes_permissions` */

CREATE TABLE `ts$nodes_permissions` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_NODE` INT(11) NOT NULL,
  `ID_USER` INT(11),
  `TYPE` INT(1) NOT NULL DEFAULT '1',
  `BEGIN_DATE` TIMESTAMP NOT NULL,
  `EXPIRE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$NODES_PERMS_INDEX_IDNODE` (`ID_NODE`),
  KEY `TS$NODES_PERMS_INDEX_IDUSER` (`ID_USER`),
  CONSTRAINT `FK_ts$nodes_permissions` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_permissions` */

/*Table structure for table `ts$nodes_revisions` */

CREATE TABLE `ts$nodes_revisions` (
  `ID_NODE` INT(11) NOT NULL,
  `ID_USER` INT(11) DEFAULT NULL,
  `DATA` LONGTEXT,
  `MERGED` INT(1) DEFAULT NULL,
  `REVISION_DATE` TIMESTAMP NOT NULL,
  KEY `ts$nodes_revisions_id_node_idx` (`ID_NODE`),
  KEY `ts$nodes_revisions_id_user_idx` (`ID_USER`),
  CONSTRAINT `FK_ts$nodes_revisions_idnode` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ts$nodes_revisions_iduser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_revisions` */

/*Table structure for table `ts$nodes_schemas` */

CREATE TABLE `ts$nodes_schemas` (
  `ID_NODE` INT(11) NOT NULL,
  `DATA` LONGTEXT COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_NODE`),
  CONSTRAINT `FK_ts$nodes_schemas` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$nodes_schemas` */

/*Table structure for table `ts$nodes_superdata` */

CREATE TABLE `ts$nodes_superdata` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_NODE` INT(11) NOT NULL,
  `CODE_ATTRIBUTE` VARCHAR(255) COLLATE utf8_unicode_ci,
  `CODE` TEXT COLLATE utf8_unicode_ci,
  `VALUE` TEXT COLLATE utf8_unicode_ci,
  `DATA` TEXT COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID`,`ID_NODE`),
  KEY `ts$nodes_sd_id_node_idx` (`ID_NODE`),
  KEY `ts$nodes_sd_code_attribute_idx` (`CODE_ATTRIBUTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$nodes_superdata` */

/*Table structure for table `ts$nodes_superdata_revisions` */

CREATE TABLE `ts$nodes_superdata_revisions` (
  `ID_NODE` INT(11) NOT NULL,
  `ID_SUPER_DATA` INT(11) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `CODE_ATTRIBUTE` VARCHAR(255) COLLATE utf8_unicode_ci,
  `CODE` TEXT COLLATE utf8_unicode_ci,
  `VALUE` TEXT COLLATE utf8_unicode_ci,
  `DATA` TEXT COLLATE utf8_unicode_ci,
  `MERGED` INT(1) DEFAULT NULL,
  `REVISION_DATE` TIMESTAMP NOT NULL,
  KEY `ts$nodes_sd_revisions_id_node_idx` (`ID_NODE`),
  KEY `ts$nodes_sd_revisions_id_sd_idx` (`ID_SUPER_DATA`),
  KEY `ts$nodes_sd_revisions_id_user_idx` (`ID_USER`),
  KEY `ts$nodes_sd_revisions_code_att_idx` (`CODE_ATTRIBUTE`),
  KEY `ts$nodes_sd_revisions_idsuperdata` (`ID_SUPER_DATA`,`ID_NODE`),
  CONSTRAINT `FK_ts$nodes_sd_revisions_idnode` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ts$nodes_sd_revisions_idsuperdata` FOREIGN KEY (`ID_SUPER_DATA`, `ID_NODE`) REFERENCES `ts$nodes_superdata` (`ID`, `ID_NODE`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ts$nodes_sd_revisions_iduser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$nodes_superdata_revisions` */

/*Table structure for table `ts$links` */

CREATE TABLE `ts$links` (
  `ID_SOURCE` INT(11) NOT NULL,
  `TYPE_SOURCE` VARCHAR(20) NOT NULL,
  `ID_TARGET` INT(11) NOT NULL,
  `TYPE_TARGET` VARCHAR(20) NOT NULL,
  `DATA` VARCHAR(255) NOT NULL,
  `DELETE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID_SOURCE`,`TYPE_SOURCE`,`ID_TARGET`,`TYPE_TARGET`,`DATA`),
  KEY `TS$LINKS_INDEX1` (`ID_SOURCE`),
  KEY `TS$LINKS_INDEX2` (`TYPE_SOURCE`),
  KEY `TS$LINKS_INDEX3` (`ID_TARGET`),
  KEY `TS$LINKS_INDEX4` (`TYPE_TARGET`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$links` */

/*Table structure for table `ts$sequences` */

CREATE TABLE `ts$sequences` (
  `CODE` VARCHAR(255) NOT NULL,
  `CODE_SUBSEQUENCE` VARCHAR(255) DEFAULT NULL,
  `VALUE` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`CODE`),
  KEY `TS$SEQUENCES_INDEX_CODE` (`CODE`),
  KEY `TS$SEQUENCES_INDEX_CODE_SUBSEQUENCE` (`CODE_SUBSEQUENCE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$sequences` */

/*Table structure for table `ts$services` */

CREATE TABLE `ts$services` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CODE` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `ENABLE` INT(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$services` */

/*Table structure for table `ts$service_auths` */

CREATE TABLE `ts$service_auths` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_SERVICE` INT(11) NOT NULL,
  `TOKEN` VARCHAR(255),
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_ts$services_auths` FOREIGN KEY (`ID_SERVICE`) REFERENCES `ts$services` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$service_auths` */


/*Table structure for table `ts$service_providers` */

CREATE TABLE `ts$service_providers` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CODE` VARCHAR(255) NOT NULL DEFAULT '',
  `LABEL` VARCHAR(255),
  `TYPE` VARCHAR(255) NOT NULL,
  `SERVICE_URL` TEXT,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$service_providers` */

/*Table structure for table `ts$service_provider_instances` */

CREATE TABLE `ts$service_provider_instances` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `LABEL` VARCHAR(255),
  `ID_REQUEST` TEXT,
  `ID_TASK` INT(11) DEFAULT NULL,
  `ID_LOCK` INT(11) DEFAULT NULL,
  `ID_REQUEST_DOCUMENT` INT(11) DEFAULT NULL,
  `ID_RESPONSE_DOCUMENT` INT(11) DEFAULT NULL,
  `CODE_SERVICE_LINK` VARCHAR(255) DEFAULT NULL,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$service_provider_instances` */

/*Table structure for table `ts$service_instances` */

CREATE TABLE `ts$service_instances` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CODE` TEXT,
  `REMOTE_UNIT_LABEL` VARCHAR(255),
  `ID_INPUT` INT(11) DEFAULT NULL,
  `ID_OUTPUT` INT(11) DEFAULT NULL,
  `CALLBACK_URI` TEXT,
  `ID_REQUEST` TEXT,
  `RESPONSE_STATE` VARCHAR(255),
  `CREATE_DATE` TIMESTAMP NOT NULL,
  `RESPONSE_DATE` TIMESTAMP,
  `ID_TASK` INT(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$service_instances` */


CREATE TABLE `ts$reports` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_USER` INT(11) NOT NULL,
  `TITLE` VARCHAR(255) DEFAULT NULL,
  `DESCRIPTION` VARCHAR(1000) DEFAULT NULL,
  `TEMPLATE` VARCHAR(255) DEFAULT NULL,
  `DATA` LONGTEXT,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ts$reports_idUser` (`ID_USER`),
  CONSTRAINT `FK_ts$reports_idUser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT  CHARSET=latin1;



/*Table structure for table `ts$tasks` */

CREATE TABLE `ts$tasks` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TARGET` INT(11) NOT NULL,
  `ID_INPUT` INT(11) DEFAULT NULL,
  `ID_OUTPUT` INT(11) DEFAULT NULL,
  `TYPE` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `LABEL` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `DESCRIPTION` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `STATE` VARCHAR(1000),
  `SENDER` TEXT,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  `EXPIRE_DATE` TIMESTAMP NULL,
  `FINISH_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  `DATA` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `ABORTED` INT(1) DEFAULT '0',
  `ORDERING` BIGINT(20) DEFAULT NULL,
  `HAS_FAILURES` INT(1) DEFAULT '0',
  `MAIN_LOCK_TYPE` VARCHAR(255) NOT NULL DEFAULT 'none',
  PRIMARY KEY (`ID`),
  KEY `TS$TASKS_INDEX_DELETEDATE` (`FINISH_DATE`),
  KEY `TS$TASKS_INDEX_IDNODE` (`ID_TARGET`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*  FULLTEXT(`LABEL`, `DESCRIPTION`, `DATA`) */

CREATE TABLE `ts$tasks_search` SELECT `ID`,`LABEL`,`DESCRIPTION`,`DATA` FROM `ts$tasks`;
ALTER TABLE `ts$tasks_search` ENGINE=MyISAM;
ALTER TABLE `ts$tasks_search` ADD FULLTEXT (`LABEL`, `DESCRIPTION`,`DATA`);

DELIMITER $$

CREATE
    TRIGGER `tr_ts$tasks_insert` AFTER INSERT
    ON `ts$tasks`
    FOR EACH ROW BEGIN
        INSERT INTO `ts$tasks_search` (`ID`,`LABEL`,`DESCRIPTION`,`DATA`) VALUES (new.`ID`,new.`LABEL`,new.`DESCRIPTION`,new.`DATA`);
END$$

CREATE
    TRIGGER `tr_ts$tasks_update` AFTER UPDATE
    ON `ts$tasks`
    FOR EACH ROW BEGIN
        UPDATE `ts$tasks_search` SET `LABEL`=new.`LABEL`,`DESCRIPTION`=new.`DESCRIPTION`,`DATA`=new.`DATA` WHERE `ID` = new.`ID`;
END$$

CREATE
    TRIGGER `tr_ts$tasks_delete` AFTER DELETE
    ON `ts$tasks`
    FOR EACH ROW BEGIN
        DELETE FROM `ts$tasks_search` WHERE `ID` = old.`ID`;
END$$

DELIMITER ;

/*Data for the table `ts$tasks` */

/*Table structure for table `ts$tasks_definition` */

CREATE TABLE `ts$tasks_definition` (
  `CODE` VARCHAR(7),
  `LABEL` TEXT,
  `LANGUAGE` VARCHAR(7),
  PRIMARY KEY (`CODE`,`LANGUAGE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$tasks` */

/*Table structure for table `ts$tasks_enrolments` */


CREATE TABLE `ts$tasks_enrolments` (
  `ID_TASK` INT(11) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `CREATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID_TASK`,`ID_USER`),
  KEY `TS$TEAMS_USERS_TS$U_FK1` (`ID_USER`),
  CONSTRAINT `TS$TASKS_ENROLLTS$T_FK1` FOREIGN KEY (`ID_TASK`) REFERENCES `ts$tasks` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TS$TASKS_USERS_TS$U_FK1` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$tasks_enrolments` */

/*Table structure for table `ts$tasks_facts` */

CREATE TABLE `ts$tasks_facts` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TASK` INT(11) NOT NULL,
  `ID_USER` TEXT NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `WORKLINE_CODE` VARCHAR(7),
  `WORKSTOP_CODE` VARCHAR(7),
  `EXTRA_DATA` TEXT,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_TASK` (`ID_TASK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$tasks_facts` */

/*Table structure for table `ts$tasks_locks` */

CREATE TABLE `ts$tasks_locks` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TASK` INT(11) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `MAIN` INT(1) NOT NULL DEFAULT '0',
  `DATA` TEXT,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `NewIndex1` (`ID_TASK`),
  CONSTRAINT `FK_ts$tasks_locks` FOREIGN KEY (`ID_TASK`) REFERENCES `ts$tasks` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$tasks_locks` */

/*Table structure for table `ts$cubes` */

CREATE TABLE `ts$cubes` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TYPE` VARCHAR(255) NOT NULL,
  `LABEL` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `DESCRIPTION` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `DATA` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `CREATE_DATE` TIMESTAMP NOT NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$CUBES_TYPE` (`TYPE`(64))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*  FULLTEXT(`LABEL`, `DESCRIPTION`, `DATA`) */

CREATE TABLE `ts$cubes_search` SELECT `ID`,`LABEL`,`DESCRIPTION`,`DATA` FROM `ts$cubes`;
ALTER TABLE `ts$cubes_search` ENGINE=MyISAM;
ALTER TABLE `ts$cubes_search` ADD FULLTEXT (`LABEL`, `DESCRIPTION`,`DATA`);

DELIMITER $$

CREATE
    TRIGGER `tr_ts$cubes_insert` AFTER INSERT
    ON `ts$cubes`
    FOR EACH ROW BEGIN
        INSERT INTO `ts$cubes_search` (`ID`,`LABEL`,`DESCRIPTION`,`DATA`) VALUES (new.`ID`,new.`LABEL`,new.`DESCRIPTION`,new.`DATA`);
END$$

CREATE
    TRIGGER `tr_ts$cubes_update` AFTER UPDATE
    ON `ts$cubes`
    FOR EACH ROW BEGIN
        UPDATE `ts$cubes_search` SET `LABEL`=new.`LABEL`,`DESCRIPTION`=new.`DESCRIPTION`,`DATA`=new.`DATA` WHERE `ID` = new.`ID`;
END$$

CREATE
    TRIGGER `tr_ts$cubes_delete` AFTER DELETE
    ON `ts$cubes`
    FOR EACH ROW BEGIN
        DELETE FROM `ts$cubes_search`  WHERE `ID` = old.`ID`;
END$$

DELIMITER ;



/*Data for the table `ts$cubes` */

/*Table structure for table `ts$cubes_reports` */

CREATE TABLE `ts$cubes_reports` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_CUBE` INT(11) NOT NULL,
  `LABEL` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `DESCRIPTION` TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `DATA` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `CREATE_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  `IS_VALID` INT(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `ID_CUBE` (`ID_CUBE`),
  CONSTRAINT `FK_ts$cubes_reports` FOREIGN KEY (`ID_CUBE`) REFERENCES `ts$cubes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$cubes_reports` */

/*Table structure for table `ts$thesaurus` */

CREATE TABLE `ts$thesaurus` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CODE` VARCHAR(255) NOT NULL,
  `NAME` VARCHAR(255) NOT NULL,
  `LOCATION` LONGTEXT,
  `RECURRENCY` VARCHAR(255) DEFAULT NULL,
  `UPDATE_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$NODES_CODE` (`CODE`(64))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$thesaurus` */

/*Table structure for table `ts$users` */

CREATE TABLE `ts$users` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(200) NOT NULL,
  `EMAIL` TEXT,
  `PREFERENCES` TEXT,
  `STATUS` INT(1) NOT NULL,
  `HUMAN` INT(1) NOT NULL,
  `REGISTER_DATE` TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$USERS_INDEX_USERNAME` (`USERNAME`(64))
) ENGINE=InnoDB AUTO_INCREMENT=359 DEFAULT CHARSET=utf8;

/*Data for the table `ts$users` */

INSERT  INTO `ts$users`(`ID`,`USERNAME`,`EMAIL`,`PREFERENCES`,`STATUS`,`REGISTER_DATE`) VALUES (1,'master','master@yourcompany.com',NULL,1,'2010-06-04 08:57:28'),(-1,'system','system@yourcompany.com',NULL,1,'2010-06-04 08:57:28');

/*Table structure for table `ts$users_nodes` */

CREATE TABLE `ts$users_nodes` (
  `ID_USER` INT(11) NOT NULL,
  `ID_NODE` INT(11) NOT NULL,
  `ROLE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID_USER`,`ID_NODE`,`ROLE`),
  KEY `TS$USERS_NODES_INDEX_IDNODE` (`ID_NODE`),
  KEY `TS$USERS_NODES_INDEX_IDUSER` (`ID_USER`),
  KEY `TS$USERS_NODES_INDEX_ROLE` (`ROLE`),
  CONSTRAINT `SYS_C008828` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$users_nodes` */

/*Table structure for table `ts$users_roles` */

CREATE TABLE `ts$users_roles` (
  `NAME` VARCHAR(255) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `BEGIN_DATE` TIMESTAMP NOT NULL,
  `EXPIRE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`NAME`(64),`ID_USER`),
  KEY `TS$USERS_ROLES_INDEX1` (`NAME`(64)),
  KEY `TS$USERS_ROLES_INDEX2` (`ID_USER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$users_roles` */

/*Table structure for table `ts$users_skills` */

CREATE TABLE `ts$users_skills` (
  `NAME` VARCHAR(255) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `BEGIN_DATE` TIMESTAMP NOT NULL,
  `EXPIRE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`NAME`,`ID_USER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ts$users_skills` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

