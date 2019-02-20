ALTER TABLE `ts$messagequeue` CHANGE MAILBOX URI VARCHAR(255) NOT NULL;
ALTER TABLE `ts$sources` CHANGE URL URI VARCHAR(255) NULL;

DROP PROCEDURE IF EXISTS `removeMessageQueueURLs`;
CREATE PROCEDURE `removeMessageQueueURLs`()
BEGIN
  DECLARE done INT::SEMICOLON::
  DECLARE databaseName TEXT::SEMICOLON::
  DECLARE rowIdentifier INT(11)::SEMICOLON::
  DECLARE rowUri VARCHAR(255)::SEMICOLON::
  DECLARE ListMessageQueueItems CURSOR FOR SELECT id, uri FROM `ts$messagequeue` WHERE URI IS NOT NULL::SEMICOLON::
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1::SEMICOLON::    

  SELECT DATABASE() INTO databaseName::SEMICOLON::
  SET FOREIGN_KEY_CHECKS=0::SEMICOLON::
  
  SET done = 0::SEMICOLON::
  OPEN ListMessageQueueItems::SEMICOLON::
  REPEAT
    FETCH ListMessageQueueItems INTO rowIdentifier, rowUri::SEMICOLON::
    IF NOT done THEN
	  SET @isService = LOCATE("/service/business/", rowUri) != 0::SEMICOLON::
	  IF @isService THEN
		SET @spaceName = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/', 4), '/', -1)::SEMICOLON::
		SET @serviceId = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/service/business/', -1), '/', 1)::SEMICOLON::
		SET @uri = CONCAT('msu://', @spaceName, ':', @serviceId)::SEMICOLON::
		SET @s = CONCAT('UPDATE ts$messagequeue SET uri=\'', @uri, '\' WHERE id=', rowIdentifier)::SEMICOLON::      
		PREPARE stmt FROM @s::SEMICOLON::
		EXECUTE stmt::SEMICOLON::     
	  END IF::SEMICOLON::
	
	  SET @isMailBox = LOCATE("/service/mailbox/", rowUri) != 0::SEMICOLON::
	  IF @isMailBox THEN
		SET @spaceName = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/', 4), '/', -1)::SEMICOLON::
		SET @mailboxId = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/service/mailbox/', -1), '/', 1)::SEMICOLON::
		SET @uri = CONCAT('mmu://', @spaceName, ':', @mailboxId)::SEMICOLON::
		SET @s = CONCAT('UPDATE ts$messagequeue SET uri=\'', @uri, '\' WHERE id=', rowIdentifier)::SEMICOLON::      
		PREPARE stmt FROM @s::SEMICOLON::
		EXECUTE stmt::SEMICOLON::     
	  END IF::SEMICOLON::

	  SET @isSource = LOCATE("/service/source/", rowUri)::SEMICOLON::
	  IF @isSource THEN
		SET @spaceName = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/', 4), '/', -1)::SEMICOLON::
		SET @sourceId = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/service/source/', -1), '/', 1)::SEMICOLON::
		SET @uri = CONCAT('mfu://', @spaceName, ':', @sourceId)::SEMICOLON::
		SET @s = CONCAT('UPDATE ts$messagequeue SET uri=\'', @uri, '\' WHERE id=', rowIdentifier)::SEMICOLON::      
        PREPARE stmt FROM @s::SEMICOLON::
        EXECUTE stmt::SEMICOLON::     
	  END IF::SEMICOLON::
    END IF::SEMICOLON::
  UNTIL done END REPEAT::SEMICOLON::
  CLOSE ListMessageQueueItems::SEMICOLON::
  SET FOREIGN_KEY_CHECKS=1::SEMICOLON::
END;
CALL `removeMessageQueueURLs`();
DROP PROCEDURE `removeMessageQueueURLs`;

DROP PROCEDURE IF EXISTS `removeSourceURLs`;
CREATE PROCEDURE `removeSourceURLs`()
BEGIN
  DECLARE done INT::SEMICOLON::
  DECLARE databaseName TEXT::SEMICOLON::
  DECLARE rowIdentifier INT(11)::SEMICOLON::
  DECLARE rowUri VARCHAR(255)::SEMICOLON::
  DECLARE ListSources CURSOR FOR SELECT id, uri FROM `ts$sources` WHERE uri IS NOT NULL::SEMICOLON::
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1::SEMICOLON::    

  SELECT DATABASE() INTO databaseName::SEMICOLON::
  SET FOREIGN_KEY_CHECKS=0::SEMICOLON::
  
  SET done = 0::SEMICOLON::
  OPEN ListSources::SEMICOLON::
  REPEAT
    FETCH ListSources INTO rowIdentifier, rowUri::SEMICOLON::
    IF NOT done THEN
	  SET @isService = LOCATE("/service/business/", rowUri)::SEMICOLON::
	  IF @isService THEN
		SET @spaceName = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/', 4), '/', -1)::SEMICOLON::
		SET @serviceId = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/service/business/', -1), '/', 1)::SEMICOLON::
		SET @uri = CONCAT('msu://', @spaceName, ':', @serviceId)::SEMICOLON::
		SET @s = CONCAT('UPDATE ts$sources SET uri=\'', @uri, '\' WHERE id=', rowIdentifier)::SEMICOLON::      
        PREPARE stmt FROM @s::SEMICOLON::
        EXECUTE stmt::SEMICOLON::     
	  END IF::SEMICOLON::
	  
	  SET @isMailBox = LOCATE("/service/mailbox/", rowUri)::SEMICOLON::
	  IF @isMailBox THEN
		SET @spaceName = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/', 4), '/', -1)::SEMICOLON::
		SET @sourceId = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/service/mailbox/', -1), '/', 1)::SEMICOLON::
		SET @uri = CONCAT('mmu://', @spaceName, ':', @sourceId)::SEMICOLON::
		SET @s = CONCAT('UPDATE ts$sources SET uri=\'', @uri, '\' WHERE id=', rowIdentifier)::SEMICOLON::      
        PREPARE stmt FROM @s::SEMICOLON::
        EXECUTE stmt::SEMICOLON::     
	  END IF::SEMICOLON::
	
	  SET @isSource = LOCATE("/service/source/", rowUri)::SEMICOLON::
	  IF @isSource THEN
		SET @spaceName = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/', 4), '/', -1)::SEMICOLON::
		SET @sourceId = SUBSTRING_INDEX(SUBSTRING_INDEX(rowUri, '/service/source/', -1), '/', 1)::SEMICOLON::
		SET @uri = CONCAT('mfu://', @spaceName, ':', @sourceId)::SEMICOLON::
		SET @s = CONCAT('UPDATE ts$sources SET uri=\'', @uri, '\' WHERE id=', rowIdentifier)::SEMICOLON::      
        PREPARE stmt FROM @s::SEMICOLON::
        EXECUTE stmt::SEMICOLON::     
	  END IF::SEMICOLON::
    END IF::SEMICOLON::
  UNTIL done END REPEAT::SEMICOLON::
  CLOSE ListSources::SEMICOLON::

  SET FOREIGN_KEY_CHECKS=1::SEMICOLON::
END;
CALL `removeSourceURLs`();
DROP PROCEDURE `removeSourceURLs`;