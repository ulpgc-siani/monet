DROP TABLE IF EXISTS `ds$info`;
CREATE TABLE `ds$info` (
  `NAME` VARCHAR(255) NOT NULL,
  `VALUE` VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8;
INSERT INTO ds$info (`name`, `value`) VALUES ('version', '3.2.1');

ALTER TABLE ds$documents ADD COLUMN (`created_date` datetime NOT NULL);
ALTER TABLE ds$documents_data DROP `data`;
ALTER TABLE ds$documents_data ADD COLUMN (`location` VARCHAR(255));
ALTER TABLE ds$documents_preview_data ADD COLUMN (`created_date` datetime NOT NULL);
