
// SETUP

INSERT INTO fs$users(CODE, idUser, PASSWORD, email, lang, nickname)VALUES (10,'manager', '81dc9bdb52d04dc20036dbd8313ed055', 'manager@mail.com', 'es','Manager');
INSERT INTO fs$units (CODE, NAME, HOST, url, secret)VALUES(10, 'default', 'localhost', '','1234');

// USUARIOS

INSERT INTO fs$users(CODE, idUser, PASSWORD, email, lang, nickname,human)VALUES (11,'externa', '81dc9bdb52d04dc20036dbd8313ed055', 'externa@mail.com', 'es','Externa','1');
INSERT INTO fs$units (CODE, NAME, HOST, url, secret)VALUES(10, 'default', 'localhost', '','1234');
