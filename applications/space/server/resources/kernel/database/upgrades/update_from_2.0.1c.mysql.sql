TRUNCATE TABLE ts$nodes_revisions;
ALTER TABLE ts$nodes_revisions ADD COLUMN ID INT(11) NOT NULL;
ALTER TABLE ts$nodes_revisions ADD PRIMARY KEY (ID);
ALTER TABLE ts$nodes_revisions MODIFY COLUMN ID INT(11) NOT NULL AUTO_INCREMENT;