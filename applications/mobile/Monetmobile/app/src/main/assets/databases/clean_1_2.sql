ALTER TABLE definitions ADD COLUMN label_to_show TEXT;

UPDATE definitions SET label_to_show = label;

CREATE TABLE files (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT,
	consolidated INTEGER
); 