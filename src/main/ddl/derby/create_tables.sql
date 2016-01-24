CREATE TABLE BOARD
(
    ID CHAR(16) FOR BIT DATA PRIMARY KEY NOT NULL,
    WIDTH INT NOT NULL,
    HEIGHT INT NOT NULL,
    CREATED TIMESTAMP DEFAULT current_timestamp NOT NULL
);

CREATE TABLE TARGET
(
  BOARD   CHAR(16) FOR BIT DATA NOT NULL,
  START_X INT                   NOT NULL,
  START_Y INT                   NOT NULL,
  END_X   INT                   NOT NULL,
  END_Y   INT                   NOT NULL,
  CONSTRAINT TARGET_PK PRIMARY KEY (BOARD, START_X, START_Y, END_X, END_Y)
);
CREATE INDEX TARGET_BOARD_IDX ON TARGET (BOARD);

CREATE TABLE PLAYER
(
  ID      CHAR(16) FOR BIT DATA PRIMARY KEY   NOT NULL,
  NAME    VARCHAR(256)                        NOT NULL,
  CREATED TIMESTAMP DEFAULT current_timestamp NOT NULL
);

CREATE TABLE GAME
(
  ID      CHAR(16) FOR BIT DATA PRIMARY KEY   NOT NULL,
  PLAYER  CHAR(16) FOR BIT DATA               NOT NULL,
  BOARD   CHAR(16) FOR BIT DATA               NOT NULL,
  CREATED TIMESTAMP DEFAULT current_timestamp NOT NULL,
  CONSTRAINT GAME_BOARD_FK FOREIGN KEY (BOARD) REFERENCES BOARD (ID),
  CONSTRAINT GAME_PLAYER_FK FOREIGN KEY (PLAYER) REFERENCES PLAYER (ID)
);

CREATE TABLE SHOT
(
  GAME CHAR(16) FOR BIT DATA NOT NULL,
  X    INT                   NOT NULL,
  Y    INT                   NOT NULL,
  ORDINAL TIMESTAMP          NOT NULL,
  CONSTRAINT SHOT_GAME_FK FOREIGN KEY (GAME) REFERENCES GAME (ID)
);
CREATE INDEX SHOT_GAME_IDX ON SHOT (GAME, ORDINAL);