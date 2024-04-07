-- liquibase formatted sql
-- changeset kjurczyk:2

CREATE TABLE clipping
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    clipping_file_id    BIGINT,
    book_title          VARCHAR(255),
    author              VARCHAR(255),
    book_page           VARCHAR(10),
    book_location       VARCHAR(20),
    highlight           VARCHAR(1000),
    book_highlight_date TIMESTAMP,
    FOREIGN KEY (clipping_file_id) REFERENCES clipping_file (id)
)