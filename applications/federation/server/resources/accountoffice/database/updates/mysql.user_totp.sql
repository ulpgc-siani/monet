CREATE TABLE `fs$user_totp` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_user` VARCHAR(255) NOT NULL,
  `secret` VARCHAR(255) NOT NULL,
  `enable` INT(1) NOT NULL DEFAULT 0,
  `last_used_counter` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT UNIQUE (`id_user`)
) DEFAULT CHARSET=utf8;
