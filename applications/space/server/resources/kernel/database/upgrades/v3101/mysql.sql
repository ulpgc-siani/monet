DROP TABLE IF EXISTS `ts$info`;
CREATE TABLE `ts$info` (
  `NAME` VARCHAR(255) NOT NULL,
  `VALUE` VARCHAR(255) NOT NULL
) DEFAULT CHARSET=utf8;
INSERT INTO ts$info (`name`, `value`) VALUES ('version', '3.1');