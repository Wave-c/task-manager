CREATE TABLE task (
    id VARCHAR(40) PRIMARY KEY NOT NULL,
    title VARCHAR(127) NOT NULL,
    description VARCHAR(1023) NULL,
    deadline TIMESTAMP NULL,
    username VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL
);