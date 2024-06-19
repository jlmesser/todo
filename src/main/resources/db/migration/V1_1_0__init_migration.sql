CREATE TABLE IF NOT EXISTS tasks (
    id uuid NOT NULL PRIMARY KEY,
    description varchar(255),
    completed boolean
    );

-- todo proper db init with creation of db and user etc