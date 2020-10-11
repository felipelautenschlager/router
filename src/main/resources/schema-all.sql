DROP TABLE routes IF EXISTS;

CREATE TABLE routes (
    id           VARCHAR(50) NOT NULL,
    from_seq     INTEGER NOT NULL,
    to_seq       INTEGER NOT NULL,
    from_port    VARCHAR(10) NOT NULL,
    to_port      VARCHAR(10) NOT NULL,
    leg_duration BIGINT NOT NULL,
    count        INTEGER NOT NULL,
    points       LONGVARCHAR NOT NULL,
    PRIMARY KEY (id, from_seq, to_seq)
);

CREATE TABLE points (
    id        VARCHAR(50) NOT NULL,
    from_seq  INTEGER NOT NULL,
    to_seq    INTEGER NOT NULL,
    --latitude  DECIMAL(3,6),
    longitude DECIMAL,
    timestamp BIGINT,
    knots     DECIMAL
);

ALTER TABLE points ADD FOREIGN KEY (id, from_seq, to_seq) REFERENCES routes (id, from_seq, to_seq);
