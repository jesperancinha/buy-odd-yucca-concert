create schema if not exists ticket;

drop table if exists ticket.car_parking;

drop table if exists ticket.parking_reservation;

drop table if exists ticket.ticket_reservation;

drop table if exists ticket.drink;

drop table if exists ticket.receipt;

drop table if exists ticket.ticket_reservation_concert_day;

drop table if exists ticket.audit_log;

create table if not exists ticket.car_parking
(
    id             UUID,
    parking_number bigint    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.parking_reservation
(
    id             UUID      NOT NULL,
    reference      UUID      NOT NULL UNIQUE,
    car_parking_id UUID      NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_car_parking
        FOREIGN KEY (car_parking_id)
            REFERENCES ticket.car_parking (id)
);

create table if not exists ticket.concert_day
(
    id           UUID         NOT NULL,
    reference    UUID         NOT NULL UNIQUE,
    name         varchar(255) NULL,
    description  varchar(255) NULL,
    concert_date TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.ticket_reservation
(
    id                     UUID      NOT NULL,
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

create table if not exists ticket.drink
(
    id         UUID,
    name       varchar(255) NULL,
    width      bigint,
    height     bigint,
    shape      varchar(255),
    volume     bigint,
    price      numeric,
    created_at TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.meal
(
    id         UUID,
    coupon     UUID         NULL,
    box_type   varchar(255) NULL,
    discount   bigint,
    price      numeric,
    processed  boolean,
    created_at TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

create table if not exists ticket.drink_reservation
(
    id                    UUID,
    reference             UUID      NOT NULL UNIQUE,
    ticket_reservation_id UUID      NOT NULL,
    drink_id              UUID      NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_drink_ticket_reservation
        FOREIGN KEY (ticket_reservation_id)
            REFERENCES ticket.ticket_reservation (id),
    CONSTRAINT fk_drink_reservation
        FOREIGN KEY (drink_id)
            REFERENCES ticket.drink (id)
);

create table if not exists ticket.meal_reservation
(
    id                    UUID,
    reference             UUID      NOT NULL UNIQUE,
    coupon                UUID      NULL,
    ticket_reservation_id UUID      NOT NULL,
    meal_id UUID      NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_meal_ticket_reservation
        FOREIGN KEY (ticket_reservation_id)
            REFERENCES ticket.ticket_reservation (id),
    CONSTRAINT fk_meal_reservation
        FOREIGN KEY (meal_id)
            REFERENCES ticket.meal (id)
);

create table ticket.receipt
(
    id                    UUID,
    reference             UUID,
    created_at            TIMESTAMP DEFAULT LOCALTIMESTAMP,
    ticket_reservation_id UUID NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_reservation
        FOREIGN KEY (ticket_reservation_id)
            REFERENCES ticket.ticket_reservation (id)
);

create table ticket.ticket_reservation_concert_day
(
    id                    UUID NOT NULL,
    ticket_reservation_id UUID,
    concert_day_id        UUID,
    CONSTRAINT fk_ticket_reservation
        FOREIGN KEY (ticket_reservation_id)
            REFERENCES ticket.ticket_reservation (id),
    CONSTRAINT fk_concert_day
        FOREIGN KEY (concert_day_id)
            REFERENCES ticket.concert_day (id)
);

create table ticket.audit_log
(
    id             UUID NOT NULL,
    audit_log_type VARCHAR,
    payload        VARCHAR,
    created_at     TIMESTAMP DEFAULT LOCALTIMESTAMP
)
