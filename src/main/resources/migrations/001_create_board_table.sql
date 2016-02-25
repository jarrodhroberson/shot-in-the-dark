--liquibase formatted sql

--changeset jhr:1
CREATE TABLE SITD.BOARD
(
  ID      CHAR(36) PRIMARY KEY                NOT NULL,
  WIDTH   INT                                 NOT NULL,
  HEIGHT  INT                                 NOT NULL,
  CREATED TIMESTAMP DEFAULT current_timestamp NOT NULL
);
