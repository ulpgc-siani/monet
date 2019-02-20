DELIMITER $$

DROP PROCEDURE IF EXISTS `updateurlservices`$$
CREATE PROCEDURE `updateurlservices`(url TEXT)
BEGIN
  DECLARE done INT;
  DECLARE nrow INT;
  DECLARE localmailbox TEXT;
  DECLARE replymailbox TEXT;
  DECLARE aux TEXT;
  DECLARE ListServices CURSOR FOR SELECT `ID`,`LOCAL_MAILBOX`,`REPLY_MAILBOX` FROM ts$services;

  SET done = 0;
  OPEN ListServices;
  REPEAT
    FETCH ListServices INTO nrow,localmailbox,replymailbox;
    IF NOT done THEN
    
      SET aux = SUBSTRING(localmailbox,LOCATE('//',localmailbox)+2);
      SET aux = CONCAT("http://",url,"/", SUBSTRING(aux,LOCATE('/',aux)+1));    
      SET @s = CONCAT('UPDATE ts$services SET `LOCAL_MAILBOX` = "',aux,'" WHERE `ID`=',nrow);
      PREPARE stmt FROM @s;
      EXECUTE stmt;

      SET aux = SUBSTRING(replymailbox,LOCATE('//',replymailbox)+2);
      SET aux = CONCAT("http://",url,"/", SUBSTRING(aux,LOCATE('/',aux)+1));   
      SET @s = CONCAT('UPDATE ts$services SET `REPLY_MAILBOX` = "',aux,'" WHERE `ID`=',nrow);
      PREPARE stmt FROM @s;
      EXECUTE stmt;      
      
    END IF;
  UNTIL done END REPEAT;
  CLOSE ListServices;

END$$

DROP PROCEDURE IF EXISTS `updateurlmessages`$$
CREATE PROCEDURE `updateurlmessages`(url TEXT)
BEGIN
  DECLARE done INT;
  DECLARE nrow INT;
  DECLARE mailboxtext VARCHAR(255);
  DECLARE aux TEXT;
  DECLARE ListMessages CURSOR FOR SELECT `ID`,`MAILBOX` FROM ts$messagequeue;

  SET done = 0;
  SET nrow = 0;
  OPEN ListMessages;
  REPEAT
    FETCH ListMessages INTO nrow,mailboxtext;
    IF NOT done THEN
      SET aux = SUBSTRING(mailboxtext,LOCATE('//',mailboxtext)+2);
      SET aux = CONCAT("http://",url,"/", SUBSTRING(aux,LOCATE('/',aux)+1));    
      SET @s = CONCAT('UPDATE ts$messagequeue SET `MAILBOX` = "',aux,'" WHERE `ID`=',nrow);
      PREPARE stmt FROM @s;
      EXECUTE stmt;
    END IF;
  UNTIL done END REPEAT;
  CLOSE ListMessages;

END$$

DELIMITER ;

CALL `updateurlservices`("domain:port");
CALL `updateurlmessages`("domain:port");

DROP PROCEDURE IF EXISTS `updateurlservices`;
DROP PROCEDURE IF EXISTS `updateurlmessages`;
