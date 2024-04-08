-- liquibase formatted sql
-- changeset kjurczyk:1

CREATE TABLE clipping_file
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255),
    user_id   BIGINT NOT NULL
)