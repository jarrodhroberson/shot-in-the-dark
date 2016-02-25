--liquibase formatted sql

--changeset jhr:3
CREATE TABLE SITD.PLAYER
(
  ID      CHAR(36) PRIMARY KEY                NOT NULL,
  NAME    VARCHAR(256)                        NOT NULL,
  CREATED TIMESTAMP DEFAULT current_timestamp NOT NULL
);