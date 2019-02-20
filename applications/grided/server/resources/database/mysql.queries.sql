## SERVERS

LOAD_SERVER = SELECT s.id AS id, s.name AS name, s.ip AS ip, s.enabled AS enabled, f.id AS federation_id, f.name AS federation_name  FROM servers s LEFT JOIN components f ON( f.parent_id = s.id AND f.type = '0') WHERE s.id=@id
LOAD_SERVERS = SELECT id, name, ip, enabled FROM servers
INSERT_SERVER = INSERT INTO servers(name, ip, enabled) VALUES(@name, @ip, @enabled)
UPDATE_SERVER = UPDATE servers SET name=@name, ip=@ip, enabled=@enabled WHERE id=@id
DELETE_SERVER = DELETE FROM servers where id=@id
LOAD_SERVER_ID_OF_FEDERATION =SELECT f.parent_id as id FROM components f WHERE TYPE='0' AND f.id=@federation_id

## FEDERATIONS

LOAD_FEDERATION   = SELECT c1.id AS id, c1.name AS name, c1.state as state, c1.running_time as running_time, s.id AS server_id, s.name AS server_name, s.ip AS server_ip, c2.id AS space_id, c2.name AS space_name  FROM components c1 JOIN (servers s) ON (c1.parent_id = s.id)  LEFT JOIN (components c2) ON (c1.id = c2.parent_id AND c2.type = "1")  WHERE c1.id=@id AND c1.type='0' 
LOAD_SERVER_FEDERATIONS  = SELECT c.id AS id, c.name AS name, c.state as state, c.running_time as running_time, s.id AS server_id, s.name AS server_name, s.ip as server_ip  FROM components c JOIN (servers s) ON (c.parent_id = s.id) WHERE c.type='0'
LOAD_FEDERATIONS  = SELECT c.id AS id, c.name AS name, c.state as state, c.running_time as running_time, s.id AS server_id, s.name AS server_name, s.ip as server_ip FROM components c JOIN (servers s) ON (c.parent_id = s.id) WHERE c.type='0'
LOAD_FEDERATION_OF_SPACE = SELECT f.id AS id, f.name AS name, f.state as state, f.running_time as running_time, b.id AS space_id, b.name as space_name, s.id AS server_id, s.name AS server_name, s.ip AS server_ip FROM components f, components b, servers s WHERE b.parent_id = f.id AND f.parent_id = s.id AND b.id = @space_id AND b.type='1'  
INSERT_FEDERATION = INSERT INTO components(name, parent_id, type, state, running_time) VALUES(@name, @server_id, @type, @state, @running_time)
UPDATE_FEDERATION = UPDATE components SET name=@name, parent_id=@server_id, state=@state, running_time=@running_time WHERE id=@id and type='0'
DELETE_FEDERATION = DELETE FROM components WHERE id=@id AND type='0'

## SPACES

LOAD_SPACE = SELECT c1.id AS id, c1.name AS NAME, c1.model_version_id AS model_version_id, c1.state AS state, c1.running_time AS running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name, s.ip AS server_ip, mv.id AS model_version_id, mv.label AS model_version_label, mv.metamodel_version AS metamodel_version FROM components c1 JOIN components c2 ON (c2.id = c1.parent_id) JOIN servers s ON(s.id = c2.parent_id) LEFT JOIN modelversions mv ON(c1.model_version_id = mv.id) WHERE c1.id=@id AND c1.type='1'
LOAD_FEDERATION_SPACES = SELECT c1.id AS id, c1.name AS name, c1.state as state, c1.running_time as running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name FROM components c1 JOIN components c2 ON (c2.id=@federation_id AND c2.id = c1.parent_id) JOIN servers s ON(s.id = c2.parent_id) WHERE c1.type='1'  
LOAD_SERVER_SPACES = SELECT c1.id AS id, c1.name AS name, c1.state as state, c1.running_time as running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name  FROM components c1 JOIN servers s ON(s.id=@server_id) JOIN components c2 ON(c2.parent_id = s.id AND c1.parent_id = c2.id)
LOAD_SPACES_WITH_MODEL = SELECT c1.id AS id, c1.name AS NAME, c1.state AS state, c1.running_time AS running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name, s.ip AS server_ip, mv.id AS model_version_id, mv.label AS model_version_label, mv.metamodel_version AS metamodel_version FROM components c1 JOIN components c2 ON (c2.id = c1.parent_id) JOIN modelversions mv ON(c1.model_version_id = mv.id) JOIN servers s ON(s.id = c2.parent_id)  WHERE c1.type='1' AND c1.model_version_id IN (SELECT id FROM modelversions WHERE model_id=@model_id)

##LOAD_FEDERATION_SPACES = SELECT c1.id AS id, c1.name AS name, c1.state as state, c1.running_time as running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name FROM components c1 JOIN components c2 ON (c2.id=@federation_id AND c2.id = c1.parent_id) JOIN servers s ON(s.id = c2.parent_id) WHERE c1.type='1'  
##LOAD_SERVER_SPACES = SELECT c1.id AS id, c1.name AS name, c1.state as state, c1.running_time as running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name  FROM components c1 JOIN servers s ON(s.id=@server_id) JOIN components c2 ON(c2.parent_id = s.id AND c1.parent_id = c2.id)
##LOAD_SPACES_WITH_MODEL = SELECT c1.id AS id, c1.name AS NAME, c1.state as state, c1.running_time as running_time, c2.id AS federation_id, c2.name AS federation_name, s.id AS server_id, s.name AS server_name, s.ip AS server_ip FROM components c1 JOIN components c2 ON (c2.id = c1.parent_id)  JOIN servers s ON(s.id = c2.parent_id)  WHERE c1.type='1' AND c1.model_version_id IN (select id from modelversions where model_id=@model_id) 

INSERT_SPACE = INSERT INTO components(name, parent_id, type, state, running_time) VALUES(@name, @federation_id, @type, @state, @running_time)
UPDATE_SPACE = UPDATE components SET name=@name, state=@state, running_time=@running_time WHERE id=@id AND type='1'
DELETE_SPACE = DELETE FROM components WHERE id=@id and type='1'

## MODELS

LOAD_MODELS = SELECT m.id AS id, m.name AS name from models as m
LOAD_MODEL  = SELECT m.id AS id, m.name AS NAME, mv.id AS model_version_id, mv.label AS model_version_label, mv.date AS model_version_date, mv.metamodel_version FROM models m LEFT JOIN modelversions mv ON (m.id = mv.model_id) WHERE m.id = @id
LOAD_SPACE_MODEL = SELECT m.id AS id, m.name AS name from models as m WHERE m.id = @id
INSERT_MODEL  = INSERT models(name) values(@name) 
UPDATE_MODEL  = UPDATE models SET name=@name WHERE id=@id
DELETE_MODEL  = DELETE FROM models WHERE id=@id

## MODELVERSIONS

LOAD_MODEL_VERSION  = SELECT mv.id, mv.label, mv.date, mv.metamodel_version , m.id AS model_id, m.name AS model_name FROM modelversions mv, models m WHERE mv.id = @id AND mv.model_id = m.id
LOAD_MODEL_VERSIONS = SELECT mv.id, mv.label, mv.date, mv.metamodel_version, m.id AS model_id, m.name AS model_name FROM models m, modelversions mv WHERE m.id = mv.model_id AND m.id = @model_id
LOAD_MODEL_VERSIONS_BY_METAMODEL = SELECT mv.id, mv.label, mv.date, mv.metamodel_version, m.id AS model_id, m.name AS model_name FROM models m, modelversions mv WHERE m.id = mv.model_id AND m.id = @model_id AND mv.metamodel_version = @metamodel_version 
LOAD_LATEST_MODEL_VERSION = SELECT mv.id, mv.label, mv.date, mv.metamodel_version, m.id AS model_id, m.name AS model_name FROM models m, modelversions mv WHERE m.id = mv.model_id AND m.id = @model_id AND mv.date IN (SELECT MAX(DATE) FROM modelversions mv WHERE mv.model_id = @model_id) 
INSERT_MODEL_VERSION = INSERT INTO modelversions(model_id, label, date, metamodel_version) VALUES(@model_id, @label, @date, @metamodel_version)
DELETE_MODEL_VERSION = DELETE FROM modelversions WHERE id=@id AND model_id=@model_id