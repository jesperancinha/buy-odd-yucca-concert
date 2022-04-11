create schema if not exists ticket;

drop table if exists ticket.car_parking;

drop table if exists ticket.parking_reservation;

drop table if exists ticket.ticket_reservation;

drop table if exists ticket.receipt;
drop table if exists receipt;

create table if not exists ticket.car_parking
(
    id             UUID               DEFAULT gen_random_uuid(),
    parking_number bigint    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.parking_reservation
(
    id             UUID               DEFAULT gen_random_uuid(),
    car_parking_id UUID      NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_car_parking
        FOREIGN KEY (car_parking_id)
            REFERENCES ticket.car_parking (id)
);

create table if not exists ticket.ticket_reservation
(
    id                     UUID               DEFAULT gen_random_uuid(),
    reference              UUID      NOT NULL UNIQUE,
    name                   varchar,
    address                varchar,
    birth_date             date,
    parking_reservation_id UUID      NULL,
    created_at             TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_parking_reservation
        FOREIGN KEY (parking_reservation_id)
            REFERENCES ticket.parking_reservation (id)
);

create table if not exists ticket.receipt
(
    id                    UUID               DEFAULT gen_random_uuid(),
    reference             UUID      NOT NULL UNIQUE,
    ticket_reservation_id UUID      NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_car_parking
        FOREIGN KEY (ticket_reservation_id)
            REFERENCES ticket.ticket_reservation (id)
);

create table ticket.audit_log
(
    id             UUID NOT NULL,
    audit_log_type VARCHAR,
    payload       VARCHAR,
    created_at     TIMESTAMP DEFAULT LOCALTIMESTAMP
)
