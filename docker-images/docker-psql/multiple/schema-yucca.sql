create schema if not exists parking;

alter schema parking OWNER TO kong;

drop table if exists parking.parking_reservation;

create table if not exists parking.parking_reservation
(
    id             UUID                  DEFAULT gen_random_uuid(),
    parking_number VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);