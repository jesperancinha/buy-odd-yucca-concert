create schema if not exists parking;

create schema if not exists ticket;

alter schema parking OWNER TO kong;

alter schema ticket OWNER TO kong;

drop table if exists parking.parking_reservation;

create table if not exists parking.parking_reservation
(
    id             UUID               DEFAULT gen_random_uuid(),
    parking_number bigint    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

drop table if exists ticket.ticket_reservation;

create table if not exists ticket.ticket_reservation
(
    id         UUID               DEFAULT gen_random_uuid(),
    reference  UUID      NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ticket_reservation;
