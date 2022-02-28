create schema if not exists parking;

create schema if not exists ticket;

drop table  if exists ticket.parking_reservation;

drop table  if exists ticket.ticket_reservation;

create table if not exists ticket.parking_reservation
(
    id             UUID               DEFAULT gen_random_uuid(),
    parking_number bigint    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.ticket_reservation
(
    id                    UUID               DEFAULT gen_random_uuid(),
    name                  varchar,
    address               varchar,
    birth_date            date,
--     concert_days          UUID,
--     meals                 UUID,
    car_parking_ticket_id UUID      NULL,
    reference             UUID      NOT NULL UNIQUE,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_car_parking_ticket
        FOREIGN KEY (car_parking_ticket_id)
            REFERENCES ticket.parking_reservation (id)
);
