LOAD_FEDERATIONS=SELECT id, name, uri, label, trusted FROM fs$federations
LOAD_TRUSTED_FEDERATIONS=SELECT id, name, uri, label, trusted FROM fs$federations WHERE trusted=1
LOAD_FEDERATION=SELECT id, name, uri, label, trusted FROM fs$federations WHERE name=@name
LOAD_FEDERATION_BY_ID=SELECT id, name, uri, label, trusted FROM fs$federations WHERE id=@id
EXISTS_FEDERATION=SELECT name FROM fs$federations WHERE name=@name
CREATE_FEDERATION=INSERT INTO fs$federations (name, uri, label, trusted) VALUES (@name, @uri, @label, @trusted)
SAVE_FEDERATION=UPDATE fs$federations SET name=@name, uri=@uri, label=@label, trusted=@trusted WHERE id=@id
REMOVE_FEDERATION=DELETE FROM fs$federations WHERE id=@id

CREATE_FEDERATION_TRUST_REQUEST=INSERT INTO fs$federation_trust_requests (id_federation, validation_code, create_date) VALUES (@id_federation, @validation_code, @create_date)
EXISTS_FEDERATION_TRUST_REQUEST=SELECT id_federation FROM fs$federation_trust_requests WHERE id_federation=@id_federation AND validation_code=@validation_code
REMOVE_FEDERATION_TRUST_REQUEST=DELETE FROM fs$federation_trust_requests WHERE id_federation=@id_federation
  
LOAD_BUSINESS_UNITS=SELECT fs$business_units.id, fs$business_units.id_federation, fs$business_units.name, fs$business_units.label, fs$business_units.type, fs$business_units.uri, fs$business_units.secret, fs$business_units.enable, fs$business_units.visible FROM fs$business_units, fs$federations WHERE fs$federations.id=fs$business_units.id_federation AND fs$federations.name=@federation
LOAD_MEMBER_BUSINESS_UNITS=SELECT id, id_federation, name, label, type, uri, secret, enable, visible FROM fs$business_units WHERE type='member'
LOAD_PARTNER_BUSINESS_UNITS=SELECT id, id_federation, name, label, type, uri, secret, enable, visible FROM fs$business_units WHERE (type='partner' OR type='member') AND enable=1
LOAD_BUSINESS_UNIT=SELECT fs$business_units.id, fs$business_units.id_federation, fs$business_units.name, fs$business_units.label, fs$business_units.type, fs$business_units.uri, fs$business_units.secret, fs$business_units.enable, fs$business_units.visible FROM fs$business_units, fs$federations WHERE fs$business_units.name=@name AND fs$federations.id=fs$business_units.id_federation AND fs$federations.name=@federation
EXISTS_BUSINESS_UNIT_BY_ID=SELECT id FROM fs$business_units WHERE id=@id
LOAD_BUSINESS_UNIT_BY_ID=SELECT id, id_federation, name, label, type, uri, secret, enable, visible FROM fs$business_units WHERE id=@id
LOAD_PARTNER_BUSINESS_UNIT=SELECT id, id_federation, name, label, type, uri, secret, enable, visible FROM fs$business_units WHERE name=@name
LOAD_MEMBER_BUSINESS_UNIT=SELECT id, id_federation, name, label, type, uri, secret, enable, visible FROM fs$business_units WHERE name=@name AND type='member'
EXISTS_BUSINESS_UNIT=SELECT fs$business_units.name FROM fs$business_units, fs$federations WHERE fs$business_units.name=@name AND fs$federations.id=fs$business_units.id_federation AND fs$federations.name=@federation
EXISTS_MEMBER_BUSINESS_UNIT=SELECT name FROM fs$business_units WHERE name=@name AND type='member'
CREATE_BUSINESS_UNIT=INSERT INTO fs$business_units (id_federation, name, label, type, uri, secret, enable, visible) VALUES (@id_federation, @name, @label, @type, @uri, @secret, @enable, @visible)
SAVE_BUSINESS_UNIT=UPDATE fs$business_units SET label=@label, type=@type, uri=@uri, secret=@secret, enable=@enable, visible=@visible WHERE id=@id
REMOVE_BUSINESS_UNIT=DELETE FROM fs$business_units WHERE id=@id

CREATE_BUSINESS_UNIT_PARTNER_REQUEST=INSERT INTO fs$partner_requests (id_business_unit, validation_code, create_date) VALUES (@id_business_unit, @validation_code, @create_date)
EXISTS_BUSINESS_UNIT_PARTNER_REQUEST=SELECT id_business_unit FROM fs$partner_requests WHERE id_business_unit=@id_business_unit AND validation_code=@validation_code
REMOVE_BUSINESS_UNIT_PARTNER_REQUEST=DELETE FROM fs$partner_requests WHERE id_business_unit=@id_business_unit

LOAD_BUSINESS_UNIT_SERVICES=SELECT id_business_unit, name, label, ontology, enable FROM fs$services WHERE id_business_unit=@id_business_unit
CREATE_BUSINESS_UNIT_SERVICE=INSERT INTO fs$services(id_business_unit, name, label, ontology, enable) VALUES (@id_business_unit, @name, @label, @ontology, @enable)
CLEAR_BUSINESS_UNIT_SERVICES=DELETE FROM fs$services WHERE id_business_unit=@id_business_unit

LOAD_BUSINESS_UNIT_FEEDERS=SELECT id_business_unit, name, label, ontology, enable FROM fs$feeders WHERE id_business_unit=@id_business_unit
CREATE_BUSINESS_UNIT_FEEDER=INSERT INTO fs$feeders(id_business_unit, name, label, ontology, enable) VALUES (@id_business_unit, @name, @label, @ontology, @enable)
CLEAR_BUSINESS_UNIT_FEEDERS=DELETE FROM fs$feeders WHERE id_business_unit=@id_business_unit

SEARCH_USERS=SELECT id, username, fullname, email, lang, human, `mode` FROM fs$users WHERE (MATCH(username) AGAINST (@condition IN BOOLEAN MODE) OR fullname LIKE @condition_like) AND human=1 LIMIT @start,@limit
SEARCH_USERS_COUNT=SELECT COUNT(id) AS counter FROM fs$users WHERE (MATCH(username) AGAINST (@condition IN BOOLEAN MODE) OR fullname LIKE @condition_like) AND human=1
EXISTS_USER=SELECT id FROM fs$users WHERE username=@id OR username=@id
LOAD_USER=SELECT id, username, fullname, email, lang, human, `mode` FROM fs$users WHERE id=@id
LOAD_USER_FROM_USERNAME=SELECT id, username, fullname, email, lang, human, `mode` FROM fs$users WHERE username=@username
LOAD_USER_FROM_EMAIL=SELECT id, username, fullname, email, lang, human, `mode` FROM fs$users WHERE email=@email
CREATE_USER=INSERT INTO fs$users (username, fullname, email, lang, human, `mode`) VALUES (@username, @fullname, @email, @lang, @human, @mode)
SAVE_USER=UPDATE fs$users SET username=@username, fullname=@fullname, email=@email, lang=@lang, human=@human, `mode`=@mode WHERE id=@id

AUTHDB_CREATE_USER=INSERT INTO fs$authdb_users (id_user, password) VALUES (@id_user, @password)
AUTHDB_LOGIN_USER=SELECT fs$users.id, username, fullname, email, lang, human FROM fs$authdb_users, fs$users WHERE username=@username AND `password`=@password AND id_user=`fs$users`.`id`
AUTHDB_LOAD_USER=SELECT id, username, fullname, email, lang, human FROM fs$users WHERE username=@username
AUTHDB_LOAD_USER_BY_EMAIL=SELECT id, username, fullname, email, lang, human FROM fs$users WHERE email=@email
AUTHDB_SAVE_USER=UPDATE fs$users SET username=@username, fullname=@fullname, email=@email, lang=@lang, human=@human WHERE username=@username
AUTHDB_EXISTS_USER_PASSWORD=SELECT id FROM fs$authdb_users WHERE id_user=@id_user
AUTHDB_CREATE_USER_PASSWORD=INSERT INTO fs$authdb_users (id_user, password) VALUES (@id_user, @password)
AUTHDB_UPDATE_USER_PASSWORD=UPDATE fs$authdb_users SET password=@password WHERE id_user=@id_user

SELECT_BANNED_IP=SELECT attempts, last_attempt FROM fs$login_attempts WHERE ip=@ip
UPDATE_BANNED_IP=UPDATE fs$login_attempts SET attempts=@attempts, last_attempt=@last_attempt WHERE ip=@ip
DELETE_BANNED_IP=DELETE FROM fs$login_attempts WHERE ip=@ip
INSERT_BANNED_IP=INSERT INTO fs$login_attempts(ip, attempts, last_attempt) VALUES (@ip, @attempts, @last_attempt)

LOAD_SESSIONS=SELECT token, username, remember_me, verifier, lang, is_mobile, last_use, `space`, node FROM fs$tokens
LOAD_SESSIONS_COUNT=SELECT count(token) FROM fs$tokens
LOAD_SESSION=SELECT token, username, remember_me, verifier, lang, is_mobile, last_use, `space`, node FROM fs$tokens WHERE token=@token
LOAD_SESSION_FROM_USERNAME=SELECT token, username, remember_me, verifier, lang, is_mobile, last_use, `space`, node FROM fs$tokens WHERE username=@username
REGISTER_SESSION=INSERT INTO fs$tokens (token, username, remember_me, verifier, lang, is_mobile, last_use, `space`, node) VALUES (@token, @username, @remember_me, @verifier, @lang, @is_mobile, @last_use, @space, @node)
SAVE_SESSION=UPDATE fs$tokens SET lang=@lang, remember_me=@remember_me, is_mobile=@is_mobile, last_use=@last_use, `space`=@space, node=@node WHERE token=@token
UNREGISTER_SESSION=DELETE FROM fs$tokens WHERE token=@token
UNREGISTER_SESSIONS=DELETE FROM fs$tokens WHERE last_use<=@last_use

LOAD_MOBILE_DEVICES=SELECT id, id_user FROM fs$mobile_devices
LOAD_USER_MOBILE_DEVICES=SELECT id, id_user FROM fs$mobile_devices WHERE id_user=@id_user
EXISTS_MOBILE_DEVICE=SELECT id, id_user FROM fs$mobile_devices WHERE id=@id AND id_user=@id_user
REGISTER_MOBILE_DEVICE=INSERT INTO fs$mobile_devices (id, id_user) VALUES (@id, @id_user)
UNREGISTER_MOBILE_DEVICE=DELETE FROM fs$mobile_devices WHERE id=@id AND id_user=@id_user
