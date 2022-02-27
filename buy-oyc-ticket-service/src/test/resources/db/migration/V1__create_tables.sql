create schema if not exists ticket;

create table if not exists ticket.ticket_reservation
(
    id                 UUID               DEFAULT gen_random_uuid(),
    name               varchar,
    address            varchar,
    birth_date         date,
    concert_days       UUID,
    meals              UUID,
    car_parking_ticket UUID,
    reference          UUID      NOT NULL UNIQUE,
    created_at         TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);
