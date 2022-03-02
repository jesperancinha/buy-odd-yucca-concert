drop schema if exists parking;

create schema if not exists ticket;

drop table if exists ticket.car_parking;

drop table if exists ticket.parking_reservation;
drop table if exists parking_reservation;

drop table if exists ticket.ticket_reservation;

create table if not exists ticket.car_parking
(
    id             UUID               DEFAULT gen_random_uuid(),
    parking_number bigint    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.parking_reservation
(
    id_pr          UUID      NOT NULL,
    car_parking_id UUID      NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id_pr),
    CONSTRAINT fk_car_parking
        FOREIGN KEY (car_parking_id)
            REFERENCES ticket.car_parking (id)
);

create table if not exists ticket.concert_day
(
    id          UUID         NOT NULL,
    name        varchar(255) NULL,
    description varchar(255) NULL,
    date        TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    created_at  TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);



create table if not exists ticket.ticket_reservation
(
    id                     UUID      NOT NULL DEFAULT gen_random_uuid(),
    name                   varchar,
    address                varchar,
    birth_date             date,
    parking_reservation_id UUID      NULL,
    reference              UUID      NOT NULL UNIQUE,
    created_at             TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_parking_reservation
        FOREIGN KEY (parking_reservation_id)
            REFERENCES ticket.parking_reservation (id_pr)
);
