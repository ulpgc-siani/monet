/*
SQLyog Community- MySQL GUI v8.22 
MySQL - 5.1.37 : Database - monet
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


/*Table structure for table `ts$info` */

CREATE TABLE `ts$info` (
  `NAME` VARCHAR(255) NOT NULL,
  `VALUE` VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8;
INSERT INTO ts$info (`name`, `value`) VALUES ('version', '3.3.2');


/*Table structure for table `ts$default_values` */

CREATE TABLE `ts$default_values` (
  `CODE_NODE` VARCHAR(100) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `PROPERTY` VARCHAR(100) NOT NULL,
  `DATA` LONGTEXT,
  KEY `CODE_NODE` (`CODE_NODE`,`ID_USER`),
  KEY `CODE_NODE_2` (`CODE_NODE`,`ID_USER`,`PROPERTY`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$default_values` */

/*Table structure for table `ts$post_types` */
CREATE TABLE ts$post_types (
  id INT(11) NOT NULL,
  label VARCHAR(255),
  PRIMARY KEY(id)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$post_types` */

INSERT INTO ts$post_types (id, label) VALUES (0, 'ServiceOrder');
INSERT INTO ts$post_types (id, label) VALUES (1, 'ServiceResponse');
INSERT INTO ts$post_types (id, label) VALUES (2, 'Info');
INSERT INTO ts$post_types (id, label) VALUES (3, 'BusinessModelUpdated');
INSERT INTO ts$post_types (id, label) VALUES (4, 'BusinessModelInstalled');
INSERT INTO ts$post_types (id, label) VALUES (5, 'UserPost');

/*Table structure for table `ts$posts` */
CREATE TABLE ts$posts (
  id INT(11) NOT NULL AUTO_INCREMENT,
  create_date TIMESTAMP NULL,
  type INT(11) NOT NULL,
  title VARCHAR(512),
  body TEXT,
  body_target VARCHAR(255),
  wall_user_id INT(11),
  target VARCHAR(255),
  target_code VARCHAR(100),
  tag VARCHAR(100),
  PRIMARY KEY(id),
  KEY `IDX_ts$posts_create_date` (`create_date`),
  CONSTRAINT FK_ts$posts_type FOREIGN KEY (`type`) REFERENCES ts$post_types (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$posts` */

/*Table structure for table `ts$post_comments` */
CREATE TABLE ts$post_comments (
  id INT(11) NOT NULL AUTO_INCREMENT,
  create_date TIMESTAMP NULL,
  id_author INT(11) NOT NULL,
  author VARCHAR(255),
  text TEXT,
  id_post INT(11) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT FK_ts$post_comments_posts FOREIGN KEY (`id_post`) REFERENCES ts$posts (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$post_comments` */

/*Table structure for table `ts$post_user_filter` */
CREATE TABLE ts$post_user_filter (
  id_user INT(11) NOT NULL,
  id_post INT(11) NOT NULL,
  PRIMARY KEY (`id_user`, `id_post`),
  CONSTRAINT FK_ts$post_user_filter FOREIGN KEY (`id_post`) REFERENCES ts$posts (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) DEFAULT CHARSET=utf8;

/*Table structure for table `ts$post_user_author_filter` */
CREATE TABLE ts$post_user_author_filter (
  id_user INT(11) NOT NULL,
  id_author INT(11) NOT NULL,
  PRIMARY KEY (`id_user`, `id_author`)
) DEFAULT CHARSET=utf8;

/*Table structure for table `ts$locations` */
CREATE TABLE ts$locations (
  id INT(11) NOT NULL AUTO_INCREMENT,
  id_node INT(11) NOT NULL,
  id_location VARCHAR(255),
  `geometry` GEOMETRY NOT NULL,
  create_date TIMESTAMP,
  PRIMARY KEY (id)
/*  CONSTRAINT FK_ts$locations_ts$nodes FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE SPATIAL INDEX ts$locations_geometry ON ts$locations (`geometry`);
/*Data for the table `ts$locations` */


/*Table structure for table `ts$workqueue` */
CREATE TABLE ts$workqueue (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` INT(11) NOT NULL,
  `state` INT(11) NOT NULL,
  `retries` INT(11) NOT NULL,
  `target` LONGTEXT NOT NULL,
  `last_error` TEXT,
  `creation_time` TIMESTAMP NULL,
  `last_update_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;
/*Data for the table `ts$workqueue` */

/*Table structure for table `ts$notifications` */
CREATE TABLE `ts$notifications` ( 
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT, 
  `id_user` INT(11) NOT NULL, 
  `id_publication` VARCHAR(255),
  `label` TEXT NOT NULL, 
  `icon` TEXT, 
  `read` INT(1) NOT NULL, 
  `target` VARCHAR(255), 
  `create_date` TIMESTAMP NULL, 
  PRIMARY KEY (`id`),
  KEY `id_publication` (`id_publication`)
) DEFAULT CHARSET=utf8;
/*Data for the table `ts$notifications` */

/*Table structure for table `ts$eventlog` */

CREATE TABLE `ts$eventlog` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `logger` VARCHAR(255) NOT NULL,
  `priority` VARCHAR(255) NOT NULL,
  `message` LONGTEXT NOT NULL,
  `stacktrace` TEXT,
  `creation_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  KEY `priority` (`priority`),
  KEY `creationTime` (`creation_time`)
) DEFAULT CHARSET=utf8;

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
  `CREATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$LOGBOOKNODE_ID_NODE` (`ID_NODE`),
  KEY `TS$LOGBOOKNODE_ID_USER` (`ID_USER`),
  KEY `TS$LOGBOOKNODE_VISITED` (`VISITED`),
  KEY `TS$LOGBOOKNODE_CREATED` (`CREATED`),
  KEY `TS$LOGBOOKNODE_DELETED` (`DELETED`),
  KEY `TS$LOGBOOKNODE_MODIFIED` (`MODIFIED`),
  KEY `TS$LOGBOOKNODE_CREATE_DATE` (`CREATE_DATE`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$logbooknode` */

/*Table structure for table `ts$logbooknode_subscribers` */

CREATE TABLE `ts$logbooknode_subscribers` (
  `SERVER_NAME` TEXT NOT NULL,
  `SERVER_HOST` TEXT NOT NULL,
  `SERVER_PORT` INT(1) NOT NULL,
  `TYPE` INT(1) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `REGISTER_DATE` TIMESTAMP NULL
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$logbooknode_subscribers` */

/*Table structure for table `ts$nodes` */


CREATE TABLE `ts$nodes` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_PARENT` INT(11),
  `ID_OWNER` INT(11),
  `ID_PROTOTYPE` int(11),
  `CODE` VARCHAR(100) NOT NULL,
  `DEFINITION_TYPE` VARCHAR(100) NOT NULL,
  `PARTNER_CONTEXT` VARCHAR(100),
  `ORDERING` INT(1) NOT NULL,
  `DATA` LONGTEXT,
  `FLAGS` LONGTEXT,
  `NOTES` LONGTEXT,
  `VALID` INT(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `TS$NODES_IDPARENT` (`ID_PARENT`),
  KEY `TS$NODES_IDOWNER` (`ID_OWNER`),
  KEY `TS$NODES_IDPROTOTYPE` (`ID_PROTOTYPE`),
  KEY `TS$NODES_CODE` (`CODE`(64)),
  KEY `TS$NODES_DEFINITION_TYPE` (`DEFINITION_TYPE`(64)),
  KEY `TS$NODES_PARTNER_CONTEXT` (`PARTNER_CONTEXT`(64)),
  KEY `TS$NODES_IDNIDP` (`ID_PROTOTYPE`,`ID`),
  FULLTEXT(`DATA`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$nodes` */

/*Table structure for table `ts$nodes_ancestors` */

CREATE TABLE `ts$nodes_ancestors` (
  `ID_ANCESTOR` INT(11) NOT NULL,
  `ID_NODE` INT(11) NOT NULL,
  `ANCESTOR_LEVEL` INT(11) NOT NULL,
  KEY `TS$NODES_ANCESTORS_IDNODE` (`ID_NODE`),
  KEY `TS$NODES_ANCESTORS_IDANCESTOR` (`ID_ANCESTOR`)
/*  CONSTRAINT `FK_ts$nodes_ancestors_id_ancestor` FOREIGN KEY (`ID_ANCESTOR`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ts$nodes_ancestors_id_node` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$nodes_ancestors` */

/*Table structure for table `ts$nodes_descriptors` */


CREATE TABLE `ts$nodes_descriptors` (
  `ID_NODE` INT(11) DEFAULT NULL,
  `ID_OWNER` INT(11) DEFAULT NULL,
  `ID_PARENT` INT(11) DEFAULT NULL,
  `CODE` TEXT,
  `LABEL` TEXT,
  `COLOR` VARCHAR(20),
  `DESCRIPTION` TEXT,
  `ORDERING` INT(11) DEFAULT NULL,
  `CREATE_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  `DELETE_DATE` TIMESTAMP NULL,
  `HIGHLIGHTED` VARCHAR(5) DEFAULT NULL,
  `EDITABLE` VARCHAR(5) DEFAULT NULL,
  `DELETABLE` VARCHAR(5) DEFAULT NULL,
  `PROTOTYPE` int(1) DEFAULT 0,
  `GEOREFERENCED` int(2) DEFAULT -1,
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
  KEY `TS$NODES_DESCRIPTORS_GEOREF` (`GEOREFERENCED`),
  KEY `TS$NODES_DESCRIPTORS_DDP` (`DELETE_DATE`,`PROTOTYPE`),
/*  CONSTRAINT `FK_ts$nodes_descriptors` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
  FULLTEXT(`LABEL`, `DESCRIPTION`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_descriptors` */

/*Table structure for table `ts$nodes_permissions` */

CREATE TABLE `ts$nodes_permissions` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_NODE` INT(11) NOT NULL,
  `ID_USER` INT(11),
  `TYPE` INT(1) NOT NULL DEFAULT '1',
  `BEGIN_DATE` TIMESTAMP NULL,
  `EXPIRE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$NODES_PERMS_INDEX_IDNODE` (`ID_NODE`),
  KEY `TS$NODES_PERMS_INDEX_IDUSER` (`ID_USER`)
/*  CONSTRAINT `FK_ts$nodes_permissions` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_permissions` */

/*Table structure for table `ts$nodes_revisions` */

CREATE TABLE `ts$nodes_revisions` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_NODE` INT(11) NOT NULL,
  `ID_USER` INT(11) DEFAULT NULL,
  `DATA` LONGTEXT,
  `MERGED` INT(1) DEFAULT NULL,
  `REVISION_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `ts$nodes_revisions_id_node_idx` (`ID_NODE`),
  KEY `ts$nodes_revisions_id_user_idx` (`ID_USER`)
/*  CONSTRAINT `FK_ts$nodes_revisions_idnode` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
/*  CONSTRAINT `FK_ts$nodes_revisions_iduser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_revisions` */

/*Table structure for table `ts$nodes_schemas` */

CREATE TABLE `ts$nodes_schemas` (
  `ID_NODE` INT(11) NOT NULL,
  `DATA` LONGTEXT,
  PRIMARY KEY (`ID_NODE`)
/*  CONSTRAINT `FK_ts$nodes_schemas` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_schemas` */

/*Table structure for table `ts$nodes_superdata` */

CREATE TABLE `ts$nodes_superdata` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_NODE` INT(11) NOT NULL,
  `CODE_ATTRIBUTE` VARCHAR(255),
  `CODE` TEXT,
  `VALUE` TEXT,
  `DATA` TEXT,
  PRIMARY KEY (`ID`,`ID_NODE`),
  KEY `ts$nodes_sd_id_node_idx` (`ID_NODE`),
  KEY `ts$nodes_sd_code_attribute_idx` (`CODE_ATTRIBUTE`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_superdata` */

/*Table structure for table `ts$nodes_superdata_revisions` */

CREATE TABLE `ts$nodes_superdata_revisions` (
  `ID_NODE` INT(11) NOT NULL,
  `ID_SUPER_DATA` INT(11) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `CODE_ATTRIBUTE` VARCHAR(255),
  `CODE` TEXT,
  `VALUE` TEXT,
  `DATA` TEXT,
  `MERGED` INT(1) DEFAULT NULL,
  `REVISION_DATE` TIMESTAMP NULL,
  KEY `ts$nodes_sd_revisions_id_node_idx` (`ID_NODE`),
  KEY `ts$nodes_sd_revisions_id_sd_idx` (`ID_SUPER_DATA`),
  KEY `ts$nodes_sd_revisions_id_user_idx` (`ID_USER`),
  KEY `ts$nodes_sd_revisions_code_att_idx` (`CODE_ATTRIBUTE`),
  KEY `ts$nodes_sd_revisions_idsuperdata` (`ID_SUPER_DATA`,`ID_NODE`),
/*  CONSTRAINT `FK_ts$nodes_sd_revisions_idnode` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
  CONSTRAINT `FK_ts$nodes_sd_revisions_idsuperdata` FOREIGN KEY (`ID_SUPER_DATA`, `ID_NODE`) REFERENCES `ts$nodes_superdata` (`ID`, `ID_NODE`) ON DELETE CASCADE ON UPDATE CASCADE
/*  CONSTRAINT `FK_ts$nodes_sd_revisions_iduser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$nodes_superdata_revisions` */

/*Table structure for table `ts$links` */

CREATE TABLE `ts$links` (
  `ID_SOURCE` INT(11) NOT NULL,
  `ID_SOURCE_COMPONENT` INT(11),
  `TYPE_SOURCE` VARCHAR(20) NOT NULL,
  `ID_TARGET` INT(11) NOT NULL,
  `TYPE_TARGET` VARCHAR(20) NOT NULL,
  `DATA` VARCHAR(255) NOT NULL,
  `DELETE_DATE` TIMESTAMP NULL,
  KEY `TS$LINKS_INDEX1` (`ID_SOURCE`),
  KEY `TS$LINKS_INDEX2` (`TYPE_SOURCE`),
  KEY `TS$LINKS_INDEX3` (`ID_TARGET`),
  KEY `TS$LINKS_INDEX4` (`TYPE_TARGET`),
  KEY `TS$LINKS_INDEX5` (`ID_SOURCE_COMPONENT`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$links` */

/*Table structure for table `ts$links` */

CREATE TABLE `ts$attachments` (
  `ID_SOURCE` INT(11) NOT NULL,
  `ID_TARGET` VARCHAR(255) NOT NULL,
  `TYPE_TARGET` VARCHAR(20) NOT NULL,
  `CODE_TARGET` VARCHAR(100) NOT NULL,
  `DATA` VARCHAR(255) NOT NULL,
  `DELETE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID_SOURCE`,`ID_TARGET`,`TYPE_TARGET`),
  KEY `TS$ATTACHMENTS_INDEX1` (`ID_SOURCE`),
  KEY `TS$ATTACHMENTSS_INDEX3` (`ID_TARGET`),
  KEY `TS$ATTACHMENTS_INDEX4` (`TYPE_TARGET`),
  KEY `TS$ATTACHMENTS_INDEX5` (`CODE_TARGET`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$links` */

/*Table structure for table `ts$sequences` */

CREATE TABLE `ts$sequences` (
  `CODE` VARCHAR(255) NOT NULL,
  `CODE_SUBSEQUENCE` VARCHAR(255) DEFAULT NULL,
  `VALUE` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`CODE`),
  KEY `TS$SEQUENCES_INDEX_CODE` (`CODE`),
  KEY `TS$SEQUENCES_INDEX_CODE_SUBSEQUENCE` (`CODE_SUBSEQUENCE`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$sequences` */

/*Table structure for table `ts$services` */

CREATE TABLE `ts$services` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CODE` TEXT,
  `REMOTE_UNIT_LABEL` VARCHAR(255),
  `LOCAL_MAILBOX` TEXT,
  `REPLY_MAILBOX` TEXT,
  `CREATE_DATE` TIMESTAMP NULL,
  `ID_TASK` INT(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$services` */

CREATE TABLE `ts$reports` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_USER` INT(11) NOT NULL,
  `TITLE` VARCHAR(255) DEFAULT NULL,
  `DESCRIPTION` VARCHAR(1000) DEFAULT NULL,
  `TEMPLATE` VARCHAR(255) DEFAULT NULL,
  `DATA` LONGTEXT,
  `CREATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ts$reports_idUser` (`ID_USER`)
/*  CONSTRAINT `FK_ts$reports_idUser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Table structure for table `ts$tasks` */

CREATE TABLE `ts$tasks` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_OWNER` INT(11) DEFAULT NULL,
  `OWNER_FULLNAME` VARCHAR(200) DEFAULT NULL,
  `ID_SENDER` INT(11) DEFAULT NULL,
  `SENDER_FULLNAME` VARCHAR(200) DEFAULT NULL,
  `ID_TARGET` INT(11) DEFAULT NULL,
  `PARTNER_CONTEXT` VARCHAR(100),
  `CODE_ROLE` VARCHAR(255) NULL,
  `LABEL` TEXT,
  `DESCRIPTION` TEXT,
  `SHORTCUTS` TEXT,
  `CODE` VARCHAR(100) NOT NULL,
  `STATE` VARCHAR(1000) DEFAULT NULL,
  `URGENT` INT(1) DEFAULT 0,
  `CLASSIFICATOR` VARCHAR(200) NULL,
  `INITIALIZER` INT(1) DEFAULT 0,
  `BACKGROUND` INT(1) DEFAULT 0,
  `NEW_MESSAGES` INT(20) DEFAULT '0',
  `CREATE_DATE` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `AVAILABLE_DATE` TIMESTAMP,
  `UPDATE_DATE` TIMESTAMP NULL DEFAULT NULL,
  `START_DATE` TIMESTAMP NULL DEFAULT NULL,
  `SUGGESTED_START_DATE` TIMESTAMP NULL DEFAULT NULL,
  `END_DATE` TIMESTAMP NULL DEFAULT NULL,
  `SUGGESTED_END_DATE` TIMESTAMP NULL DEFAULT NULL,
  `COMMENTS` TEXT,
  `GEOMETRY` GEOMETRY,
  `GEOMETRY_LABEL` TEXT,
  `DATA` LONGTEXT,
  PRIMARY KEY (`ID`),
  KEY `TS$TASKS_IDOWNER` (`ID_OWNER`),
  KEY `TS$TASKS_IDSENDER` (`ID_SENDER`),
  KEY `TS$TASKS_INDEX_CODEROLE` (`CODE_ROLE`),
  KEY `TS$TASKS_INDEX_CLASS` (`CODE`,`CLASSIFICATOR`),
  KEY `TS$TASKS_INDEX_AVAILABLEDATE` (`AVAILABLE_DATE`),
  KEY `TS$TASKS_INDEX_STARTDATE` (`START_DATE`),
  KEY `TS$TASKS_INDEX_SUGGESTEDSTARTDATE` (`SUGGESTED_START_DATE`),
  KEY `TS$TASKS_INDEX_ENDDATE` (`END_DATE`),
  KEY `TS$TASKS_INDEX_SUGGESTEDENDDATE` (`SUGGESTED_END_DATE`),
  FULLTEXT KEY `LABEL` (`LABEL`,`DESCRIPTION`,`DATA`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*CREATE SPATIAL INDEX ts$tasks_geometry ON ts$tasks (`geometry`);*/

/*Data for the table `ts$tasks` */

/*Table structure for table `ts$tasks_jobs` */

CREATE TABLE `ts$tasks_jobs` (
  `ID_TASK` INT(11) NOT NULL,
  `REQUEST` LONGBLOB, 
  `RESPONSE` LONGBLOB, 
  `CALLBACK_TASK_ID` INT(11),
  `CALLBACK_CODE` VARCHAR(100),
  `CALLBACK_ORDER_ID` INT(11),
  `CREATE_DATE` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `FINISH_DATE` TIMESTAMP,
  PRIMARY KEY (`ID_TASK`),
  KEY `TS$TASKS_JOBS_PK` (`ID_TASK`),
  KEY `TS$TASKS_JOBS_CREATEDATE` (`CREATE_DATE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$tasks_jobs` */

/*Table structure for table `ts$tasks_jobs_attachments` */

CREATE TABLE `ts$tasks_jobs_attachments` (
  `ID_TASK` INT(11) NOT NULL,
  `ID_ATTACHMENT` VARCHAR(255),
  PRIMARY KEY (`ID_TASK`, `ID_ATTACHMENT`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$tasks_jobs_attachments` */

/*Table structure for table `ts$tasks_orders` */

CREATE TABLE `ts$tasks_orders` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TASK` INT(11) DEFAULT NULL,
  `ID_SETUP_NODE` INT(11) DEFAULT NULL,
  `ID_ROLE` INT(11) DEFAULT NULL,
  `CODE` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `PARTNER_CONTEXT` VARCHAR(100) DEFAULT NULL,
  `COMMENTS` TEXT,
  `URGENT` INT(1) DEFAULT '0',
  `CLOSED` INT(1) DEFAULT '0',
  `NEW_MESSAGES` INT(20) DEFAULT '0',
  `CREATE_DATE` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `SUGGESTED_START_DATE` TIMESTAMP NULL DEFAULT NULL,
  `SUGGESTED_END_DATE` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$TASKSORDERS_IDTASK` (`ID_TASK`),
  FULLTEXT KEY `COMMENTS` (`COMMENTS`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$tasks_orders` */

/*Table structure for table `ts$tasks_orders` */

CREATE TABLE `ts$tasks_orders_chats_entries` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_ORDER` INT(11) DEFAULT NULL,
  `MESSAGE` TEXT,
  `TYPE` VARCHAR(255) NOT NULL,
  `SENT` INT(1) NOT NULL DEFAULT 0,
  `CREATE_DATE` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `TS$TASKSORDERSCHATSENTRIES_ID_ORDER` (`ID_ORDER`),
  FULLTEXT KEY `MESSAGE` (`MESSAGE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$tasks_orders` */

/*Table structure for table `ts$tasks_definition` */

CREATE TABLE `ts$tasks_definition` (
  `CODE` VARCHAR(7),
  `LABEL` TEXT,
  `LANGUAGE` VARCHAR(7),
  PRIMARY KEY (`CODE`,`LANGUAGE`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `ts$tasks` */

/*Table structure for table `ts$tasks_facts` */

CREATE TABLE `ts$tasks_facts` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TASK` INT(11) NOT NULL,
  `ID_USER` TEXT NOT NULL,
  `TITLE` TEXT,
  `SUBTITLE` TEXT,
  `EXTRA_DATA` TEXT,
  `CREATE_DATE` DECIMAL(17,3) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_TASK` (`ID_TASK`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$tasks_facts` */

/*Table structure for table `ts$tasks_timers` */

CREATE TABLE `ts$tasks_timers` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TASK` INT(11) NOT NULL,
  `TAG` VARCHAR(255) NOT NULL,
  `DELAY` BIGINT(20) NOT NULL,
  `CREATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_TASK` (`ID_TASK`)
/*  CONSTRAINT `FK_ts$tasks_timers` FOREIGN KEY (`ID_TASK`) REFERENCES `ts$tasks` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$tasks_timers` */

/*Table structure for table `ts$tasks_snapshots` */

CREATE TABLE `ts$tasks_snapshots` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_TASK` INT(11) NOT NULL,
  `DATA` LONGTEXT,
  `CREATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_TASK` (`ID_TASK`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$tasks_snapshots` */

/*Table structure for table `ts$mailboxes` */

CREATE TABLE `ts$mailboxes` (
  `ID` VARCHAR(255) NOT NULL,
  `ID_TASK` INT(11) NOT NULL,
  `CODE` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$mailboxes` */

/*Table structure for table `ts$mailboxes_permissions` */

CREATE TABLE `ts$mailboxes_permissions` (
  `ID` VARCHAR(255) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  PRIMARY KEY (`ID`,`ID_USER`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$mailboxes_permissions` */

/*Table structure for table `ts$cubes` */

CREATE TABLE `ts$cubes` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `TYPE` VARCHAR(255) NOT NULL,
  `LABEL` TEXT,
  `DESCRIPTION` TEXT,
  `DATA` LONGTEXT,
  `CREATE_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$CUBES_TYPE` (`TYPE`(64)),
  FULLTEXT(`LABEL`, `DESCRIPTION`, `DATA`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `ts$cubes` */

/*Table structure for table `ts$cubes_reports` */

CREATE TABLE `ts$cubes_reports` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_CUBE` INT(11) NOT NULL,
  `LABEL` TEXT,
  `DESCRIPTION` TEXT,
  `DATA` LONGTEXT,
  `CREATE_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  `IS_VALID` INT(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `ID_CUBE` (`ID_CUBE`)
/*  CONSTRAINT `FK_ts$cubes_reports` FOREIGN KEY (`ID_CUBE`) REFERENCES `ts$cubes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$cubes_reports` */

/*Table structure for table `ts$cubes_filtergroups` */

CREATE TABLE `ts$cubes_filtergroups` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_CUBE` INT(11) NOT NULL,
  `ID_USER` INT(11) NOT NULL,
  `LABEL` VARCHAR(255) NOT NULL,
  `DATA` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `CREATE_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `LABEL` (`LABEL`),
  KEY `ID_CUBE` (`ID_CUBE`),
  KEY `ID_USER` (`ID_USER`),
  CONSTRAINT `FK_ts$cubes_filtergroups` FOREIGN KEY (`ID_CUBE`) REFERENCES `ts$cubes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ts$cubes_filtergroups` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `ts$cubes_reports` */

/*Table structure for table `ts$sources` */

CREATE TABLE `ts$sources` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CODE` VARCHAR(255) NOT NULL,
  `NAME` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `ONTOLOGY` VARCHAR(255) NOT NULL,
  `PARTNER_NAME` VARCHAR(255),
  `PARTNER_LABEL` VARCHAR(255),
  `URI` VARCHAR(255),
  `LABEL` TEXT NOT NULL,
  `CREATE_DATE` TIMESTAMP NULL,
  `UPDATE_DATE` TIMESTAMP NULL,
  `ENABLED` VARCHAR(5) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$SOURCE_CODE` (`CODE`(64))
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$sources` */

/*Table structure for table `ts$users` */

CREATE TABLE `ts$users` (
  `ID` INT(11) NOT NULL,
  `USERNAME` VARCHAR(200) NOT NULL,
  `FULLNAME` VARCHAR(255),
  `EMAIL` TEXT,
  `PHOTO` TEXT,
  `PREFERENCES` TEXT,
  `REGISTER_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$USERS_INDEX_USERNAME` (`USERNAME`(64))
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE ts$users ADD FULLTEXT TS$USERS_USERNAME_FULLTEXT (`USERNAME`);

/*Data for the table `ts$users` */

INSERT  INTO `ts$users`(`ID`,`USERNAME`,`FULLNAME`,`EMAIL`,`PREFERENCES`,`REGISTER_DATE`) VALUES (-1,'system','Sistema','system@yourcompany.com',NULL,'2010-06-04 08:57:28');

/*Table structure for table `ts$users_nodes` */

CREATE TABLE `ts$users_nodes` (
  `ID_USER` INT(11) NOT NULL,
  `ID_NODE` INT(11) NOT NULL,
  `ROLE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID_USER`,`ID_NODE`,`ROLE`),
  KEY `TS$USERS_NODES_INDEX_IDNODE` (`ID_NODE`),
  KEY `TS$USERS_NODES_INDEX_IDUSER` (`ID_USER`),
  KEY `TS$USERS_NODES_INDEX_ROLE` (`ROLE`)
/*  CONSTRAINT `SYS_C008828` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$users_nodes` */

/*Table structure for table `ts$users_dashboards` */

CREATE TABLE `ts$users_dashboards` (
  `ID_USER` INT(11) NOT NULL,
  `ID_DASHBOARD` VARCHAR(50) NOT NULL,
  `ROLE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID_USER`,`ID_DASHBOARD`,`ROLE`),
  KEY `TS$USERS_NODES_INDEX_IDDASHBOARD` (`ID_DASHBOARD`),
  KEY `TS$USERS_NODES_INDEX_IDUSER` (`ID_USER`),
  KEY `TS$USERS_NODES_INDEX_ROLE` (`ROLE`)
/*  CONSTRAINT `SYS_C008828` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION*/
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$users_dashboards` */

/*Table structure for table `ts$masters` */

CREATE TABLE `ts$masters` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(200) NOT NULL,
  `CERTIFICATE_AUTHORITY` VARCHAR(200),
  `COLONIZER` VARCHAR(5) NOT NULL DEFAULT 'false',
  `FULLNAME` VARCHAR(255),
  `EMAIL` TEXT,
  `REGISTER_DATE` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$USERS_INDEX_USERNAME` (`USERNAME`(64))
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE ts$masters ADD FULLTEXT TS$USERS_USERNAME_FULLTEXT (`USERNAME`);

/*Table structure for table `ts$roles` */

CREATE TABLE `ts$roles` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_USER` INT(11),
  `CODE` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `BEGIN_DATE` TIMESTAMP NULL,
  `EXPIRE_DATE` TIMESTAMP NULL,
  `PARTNER_ID` VARCHAR(255),
  `PARTNER_SERVICE_NAME` VARCHAR(255),
  `CACHE` VARCHAR(255),
  PRIMARY KEY (`ID`),
  KEY `TS$ROLES_IDUSER` (`ID_USER`),
  KEY `TS$ROLES_CODE` (`CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE ts$roles ADD FULLTEXT TS$ROLES_CACHE (`CACHE`);

/*Data for the table `ts$roles` */

/*Table structure for table `ts$messagequeue` */

CREATE TABLE `ts$messagequeue` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ORDER_ID` INT(11) NOT NULL,
  `STATE` INT(11) NOT NULL,
  `RETRIES` INT(11) NOT NULL,
  `URI` VARCHAR(255) NOT NULL,
  `MESSAGE` LONGBLOB,
  `CODE` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NOT NULL,
  `HASH` VARCHAR(255) NOT NULL,
  `LAST_ERROR` TEXT,
  `CREATION_TIME` TIMESTAMP NULL,
  `LAST_UPDATE_TIME` TIMESTAMP NULL,
  PRIMARY KEY (`ID`),
  KEY `TS$MESSAGEQUEUE_STATE` (`STATE`)
) DEFAULT CHARSET=utf8;

/*Data for the table `ts$messagequeue` */

/*Table structure for table `ts$events` */

CREATE TABLE `ts$events` (
  `NAME` VARCHAR(255) NOT NULL,
  `DUE_DATE` TIMESTAMP NOT NULL,
  `DATA` LONGTEXT,
  `FIRED` VARCHAR(5) DEFAULT '0',
  PRIMARY KEY (`NAME`),
  KEY `TS$EVENTS_DUEDATE` (`DUE_DATE`),
  KEY `TS$EVENTS_FIRED` (`FIRED`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `ts$datastorequeue` */

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

/* Simulate foreing keys in MyIsam */
DELIMITER $$
DROP TRIGGER IF EXISTS `trg_ts$nodes_delete`$$
CREATE TRIGGER `trg_ts$nodes_delete` BEFORE DELETE ON ts$nodes FOR EACH ROW BEGIN 

/*  CONSTRAINT `FK_ts$nodes_descriptors` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
DELETE FROM ts$nodes_descriptors WHERE `id_node` = old.`id`;
/*  CONSTRAINT `FK_ts$nodes_permissions` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
DELETE FROM ts$nodes_permissions WHERE `id_node` = old.`id`;
/* CONSTRAINT FK_ts$locations_ts$nodes FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
DELETE FROM ts$locations WHERE `id_node` = old.`id`;
/* CONSTRAINT `FK_ts$nodes_ancestors_id_ancestor` FOREIGN KEY (`ID_ANCESTOR`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
DELETE FROM ts$nodes_ancestors WHERE `id_ancestor` = old.`id`;
/* CONSTRAINT `FK_ts$nodes_ancestors_id_node` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
DELETE FROM ts$nodes_ancestors WHERE `id_node` = old.`id`;
/*  CONSTRAINT `FK_ts$nodes_revisions_idnode` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
DELETE FROM ts$nodes_revisions WHERE `id_node` = old.`id`;
/*  CONSTRAINT `FK_ts$nodes_schemas` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
DELETE FROM ts$nodes_schemas WHERE `id_node` = old.`id`;
/*  CONSTRAINT `FK_ts$nodes_sd_revisions_idnode` FOREIGN KEY (`ID_NODE`) REFERENCES `ts$nodes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
DELETE FROM ts$nodes_superdata_revisions WHERE `id_node` = old.`id`;

END;$$
DELIMITER ;


DELIMITER $$
DROP TRIGGER IF EXISTS `trg_ts$tasks_delete`$$
CREATE TRIGGER `trg_ts$tasks_delete` BEFORE DELETE ON ts$tasks FOR EACH ROW BEGIN 

/* CONSTRAINT `TS$TASKS_ENROLLTS$T_FK1` FOREIGN KEY (`ID_TASK`) REFERENCES `ts$tasks` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,*/
DELETE FROM ts$tasks_facts WHERE `id_task` = old.`id`;
DELETE FROM ts$tasks_timers WHERE `id_task` = old.`id`;
DELETE FROM ts$tasks_snapshots WHERE `id_task` = old.`id`;
DELETE FROM ts$mailboxes WHERE `id_task` = old.`id`;

END;$$
DELIMITER ;


DELIMITER $$
DROP TRIGGER IF EXISTS `trg_ts$cubes_delete`$$
CREATE TRIGGER `trg_ts$cubes_delete` BEFORE DELETE ON ts$cubes FOR EACH ROW BEGIN 

/* CONSTRAINT `FK_ts$cubes_reports` FOREIGN KEY (`ID_CUBE`) REFERENCES `ts$cubes` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
DELETE FROM ts$cubes_reports WHERE `id_cube` = old.`id`;

END;$$
DELIMITER ;


DELIMITER $$
DROP TRIGGER IF EXISTS `trg_ts$users_delete`$$
CREATE TRIGGER `trg_ts$users_delete` BEFORE DELETE ON ts$users FOR EACH ROW BEGIN 

/*  CONSTRAINT `FK_ts$nodes_revisions_iduser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
  DELETE FROM ts$nodes_revisions WHERE `id_user` = old.`id`;
/*  CONSTRAINT `FK_ts$nodes_sd_revisions_iduser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
  DELETE FROM ts$nodes_superdata_revisions WHERE `id_user` = old.`id`;
/*  CONSTRAINT `FK_ts$reports_idUser` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE*/
  DELETE FROM ts$reports WHERE `id_user` = old.`id`;
/*  CONSTRAINT `SYS_C008828` FOREIGN KEY (`ID_USER`) REFERENCES `ts$users` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION*/
  DELETE FROM ts$users_nodes WHERE `id_user` = old.`id`;
  
  DELETE FROM ts$users_dashboards WHERE `id_user` = old.`id`;

  DELETE FROM ts$mailboxes_permissions WHERE `id_user` = old.`id`;
  
END;$$
DELIMITER ;


DELIMITER $$
DROP TRIGGER IF EXISTS `trg_ts$mailboxes_delete`$$
CREATE TRIGGER `trg_ts$mailboxes_delete` BEFORE DELETE ON ts$mailboxes FOR EACH ROW BEGIN 

  DELETE FROM ts$mailboxes_permissions WHERE `id` = old.`id`;
  
END;$$
DELIMITER ;





/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

