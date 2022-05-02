create table ticket.car_parking
(
    id             UUID,
    parking_number bigint    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.parking_reservation
(
    id             UUID      NOT NULL,
    reference      UUID      NOT NULL UNIQUE,
    car_parking_id UUID,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.concert_day
(
    id           UUID         NOT NULL,
    name         varchar(255) NULL,
    description  varchar(255) NULL,
    concert_date TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.concert_day_reservation
(
    id         UUID      NOT NULL UNIQUE,
    reference  UUID      NOT NULL,
    concert_id UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.ticket_reservation
(
    id                     UUID      NOT NULL,
    reference              UUID      NOT NULL UNIQUE,
    name                   varchar,
    address                varchar,
    birth_date             date,
    parking_reservation_id UUID      NULL,
    created_at             TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.drink
(
    id         UUID,
    name       varchar(255) NULL,
    width      bigint,
    height     bigint,
    shape      varchar(255),
    volume     bigint,
    price      numeric,
    created_at TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.meal
(
    id         UUID,
    coupon     UUID         NULL,
    box_type   varchar(255) NULL,
    discount   bigint,
    price      numeric,
    processed  boolean,
    created_at TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.drink_reservation
(
    id                    UUID,
    reference             UUID      NOT NULL UNIQUE,
    ticket_reservation_id UUID      NOT NULL,
    drink_id              UUID      NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.meal_reservation
(
    id                    UUID,
    reference             UUID      NOT NULL UNIQUE,
    coupon                UUID      NULL,
    ticket_reservation_id UUID      NOT NULL,
    meal_id               UUID      NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);

create table ticket.receipt
(
    id                    UUID,
    reference             UUID,
    created_at            TIMESTAMP DEFAULT LOCALTIMESTAMP,
    ticket_reservation_id UUID NULL
);

create table ticket.ticket_reservation_concert_day
(
    id                    UUID NOT NULL,
    ticket_reservation_id UUID,
    concert_day_id        UUID
);

create table ticket.audit_log
(
    id             UUID NOT NULL,
    audit_log_type VARCHAR,
    payload        VARCHAR,
    created_at     TIMESTAMP DEFAULT LOCALTIMESTAMP
)
