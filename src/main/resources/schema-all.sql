DROP TABLE routes IF EXISTS;

CREATE TABLE routes (
    id           VARCHAR(50) NOT NULL,
    from_seq     INTEGER NOT NULL,
    to_seq       INTEGER NOT NULL,
    from_port    VARCHAR(10) NOT NULL,
    to_port      VARCHAR(10) NOT NULL,
    leg_duration BIGINT NOT NULL,
    count        INTEGER NOT NULL,
    points       LONGVARCHAR NOT NULL
)