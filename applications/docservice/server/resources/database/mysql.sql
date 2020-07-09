/*
SQLyog Community v8.4
MySQL - 5.0.45 : Database - docserver_new_space

*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE `ds$info` (
  `NAME` VARCHAR(255) NOT NULL,
  `VALUE` VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8;
INSERT INTO ds$info (`name`, `value`) VALUES ('version', '3.2.2');

/*Table structure for table `documents` */

CREATE TABLE `ds$documents` (
  `id` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `id_template` BIGINT(20) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `created_date` DATETIME NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `id_template` (`id_template`),
  KEY `state` (`state`),
  CONSTRAINT `documents_ibfk_1` FOREIGN KEY (`id_template`) REFERENCES `ds$templates` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `documents_ibfk_2` FOREIGN KEY (`state`) REFERENCES `ds$documents_states` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `documents` */



/*Table structure for table `documents_data` */

CREATE TABLE `ds$documents_data` (
  `id_document` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `content_type` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `hash` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `xml_data` LONGBLOB,
  `location` VARCHAR(510),
  PRIMARY KEY  (`id_document`),
  CONSTRAINT `documents_data_ibfk_1` FOREIGN KEY (`id_document`) REFERENCES `ds$documents` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `documents_data` */



/*Table structure for table `documents_preview_data` */


CREATE TABLE `ds$documents_preview_data` (
  `id_document` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `page` INT(11) NOT NULL,
  `data` LONGBLOB NOT NULL,
  `content_type` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `type` TINYINT(4) NOT NULL,
  `width` INT(11) NOT NULL,
  `height` INT(11) NOT NULL,
  `aspect_ratio` DOUBLE NOT NULL,
  `created_date` DATETIME NOT NULL,
  PRIMARY KEY  (`id_document`,`page`,`type`),
  KEY `type` (`type`),
  CONSTRAINT `documents_preview_data_ibfk_1` FOREIGN KEY (`type`) REFERENCES `ds$documents_preview_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `documents_preview_data_ibfk_2` FOREIGN KEY (`id_document`) REFERENCES `ds$documents` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `documents_preview_data` */



/*Table structure for table `documents_preview_type` */


CREATE TABLE `ds$documents_preview_type` (
  `id` TINYINT(4) NOT NULL,
  `label` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `documents_preview_type` */



INSERT  INTO `ds$documents_preview_type`(`id`,`label`) VALUES (1,'Thumbnail'),(2,'Page'),(3,'delete');



/*Table structure for table `documents_states` */



CREATE TABLE `ds$documents_states` (
  `id` INT(11) NOT NULL,
  `label` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `documents_states` */



INSERT  INTO `ds$documents_states`(`id`,`label`) VALUES (1,'Editable'),(2,'Consolidated'),(3,'Locked'),(4,'Processing'),(5,'Error'),(6,'Overwritten');



/*Table structure for table `documents_type` */






CREATE TABLE `ds$documents_type` (
  `id` INT(11) NOT NULL,
  `label` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `documents_type` */

INSERT  INTO `ds$documents_type`(`id`,`label`) VALUES (1,'Open Document'),(2,'Open Xml'),(3,'Portable Document'),(4, 'Xml Document');

/*Table structure for table `templates` */
CREATE TABLE `ds$templates` (
  `code` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_document_type` INT(11) NOT NULL,
  `created_date` DATETIME NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `id_document_type` (`id_document_type`),
  CONSTRAINT `templates_ibfk_1` FOREIGN KEY (`id_document_type`) REFERENCES `ds$documents_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `templates` */

/*Table structure for table `templates_data` */

CREATE TABLE `ds$templates_data` (
  `id_template` BIGINT(20) NOT NULL,
  `data` LONGBLOB,
  `hash` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `content_type` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `signs_position` VARCHAR(255) COLLATE utf8_bin,
  KEY `id_template` (`id_template`),
  CONSTRAINT `templates_data_ibfk_1` FOREIGN KEY (`id_template`) REFERENCES `ds$templates` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `templates_data` */

/*Table structure for table `templates_parts` */

CREATE TABLE `ds$templates_parts` (
  `id_template` BIGINT(20) NOT NULL,
  `id` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `data` MEDIUMTEXT COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`,`id_template`),
  KEY `id_template` (`id_template`),
  CONSTRAINT `templates_parts_ibfk_1` FOREIGN KEY (`id_template`) REFERENCES `ds$templates` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `templates_parts` */

/*Table structure for table `templates_sign_fields` */
CREATE TABLE `ds$templates_sign_fields` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_template` BIGINT(20) NOT NULL,
  `sign_field` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_sign_fields` (`id_template`),
  CONSTRAINT `FK_sign_fields` FOREIGN KEY (`id_template`) REFERENCES `ds$templates` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `templates_sign_fields` */



/*Table structure for table `type_definition` */






CREATE TABLE `ds$type_definition` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `type_definition` */



INSERT  INTO `ds$type_definition`(`id`,`label`) VALUES (1,'category'),(2,'property');



/*Table structure for table `work_queue` */



CREATE TABLE `ds$work_queue` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_document` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  `operation` TINYINT(4) NOT NULL,
  `queue_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start_date` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `finish_date` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `state` TINYINT(4) NOT NULL,
  `error_msg` TEXT COLLATE utf8_bin,
  `extra_data` LONGBLOB,
  PRIMARY KEY  (`id`),
  KEY `operation` (`operation`),
  KEY `state` (`state`),
  KEY `id_document` (`id_document`),
  CONSTRAINT `work_queue_ibfk_1` FOREIGN KEY (`operation`) REFERENCES `ds$work_queue_operations` (`id`),
  CONSTRAINT `work_queue_ibfk_2` FOREIGN KEY (`state`) REFERENCES `ds$work_queue_states` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `work_queue` */



/*Table structure for table `work_queue_operations` */



CREATE TABLE `ds$work_queue_operations` (
  `id` TINYINT(4) NOT NULL,
  `label` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `work_queue_operations` */



INSERT  INTO `ds$work_queue_operations`(`id`,`label`) VALUES (1,'Update Document'),(2,'Consolidate Document'),(3,'Upload Preview Document');



/*Table structure for table `work_queue_states` */


CREATE TABLE `ds$work_queue_states` (
  `id` TINYINT(4) NOT NULL,
  `label` VARCHAR(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/*Data for the table `work_queue_states` */

INSERT  INTO `ds$work_queue_states`(`id`,`label`) VALUES (0,'Not started'),(1,'Pending'),(2,'In Progress'),(3,'Finish'),(4,'Error');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

