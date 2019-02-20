CREATE TABLE sources (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	label TEXT NOT NULL,
	title TEXT,
	subtitle TEXT,
	account TEXT NOT NULL,
	username TEXT NOT NULL
);

CREATE TABLE definitions (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	code TEXT NOT NULL,
	label TEXT NOT NULL,
	label_to_show TEXT,
	description TEXT,
	user_instanciable INTEGER NOT NULL, 
	source_id INTEGER NOT NULL,
	source_label TEXT,
	data TEXT,
	type TEXT
);

CREATE TABLE tasks (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	server_id TEXT,
	code TEXT NOT NULL,
	label TEXT NOT NULL,
	description TEXT,
	context TEXT,
	source_id INTEGER NOT NULL,
	source_label TEXT,
	latitude REAL,
	longitude REAL,
	urgent INTEGER,
	suggested_start_date TEXT,
	suggested_end_date TEXT,
	start_date TEXT,
	end_date TEXT,
	comments TEXT,
	step INTEGER,
	step_index INTEGER,
	step_count INTEGER,
	not_read_chats INTEGER,
	tray INTEGER,
	synchronized INTEGER
);

CREATE VIRTUAL TABLE tasks_fts USING fts3 ();

CREATE TABLE attachments (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	task_id INTEGER NOT NULL,
	label TEXT NOT NULL,
	content_type TEXT NOT NULL,
	path TEXT NOT NULL
);

CREATE TABLE task_chats (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	task_id TEXT NOT NULL,
	server_id TEXT NOT NULL,
	source_id INTEGER NOT NULL,
	message TEXT,
	datetime TEXT,
	is_out INTEGER,
	sent INTEGER
);

CREATE TABLE files (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT,
	consolidated INTEGER
); 
