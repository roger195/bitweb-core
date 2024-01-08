CREATE TABLE IF NOT EXISTS words (
    id SERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS text_file_infos (
     id SERIAL PRIMARY KEY,
     identifier UUID NOT NULL UNIQUE,
     status VARCHAR(10) NOT NULL,
     parts_amount INT NOT NULL
);
CREATE TABLE IF NOT EXISTS text_file_parts (
     id SERIAL PRIMARY KEY,
     content TEXT NOT NULL,
     part_number INT NOT NULL,
     text_file_infos_id INT,
     FOREIGN KEY (text_file_infos_id) REFERENCES text_file_infos(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS text_files_words (
     id SERIAL PRIMARY KEY,
     counter INT,
     text_file_infos_id INT,
     words_id INT,
     FOREIGN KEY (text_file_infos_id) REFERENCES text_file_infos(id) ON DELETE CASCADE,
     FOREIGN KEY (words_id) REFERENCES words(id) ON DELETE CASCADE,
     UNIQUE (text_file_infos_id, words_id)
);
