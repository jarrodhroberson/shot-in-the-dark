--liquibase formatted sql

--changeset jhr:5
CREATE TABLE SITD.PRINCIPAL
(
  ID       CHAR(36) PRIMARY KEY                NOT NULL,
  PASSWORD CHAR(40)                            NOT NULL,
  CREATED  TIMESTAMP DEFAULT current_timestamp NOT NULL
);