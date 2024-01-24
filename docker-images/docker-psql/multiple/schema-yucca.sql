CREATE SCHEMA IF NOT EXISTS ticket;

DROP TABLE IF EXISTS ticket.car_parking;

DROP TABLE IF EXISTS ticket.parking_reservation;

DROP TABLE IF EXISTS ticket.ticket_reservation;

DROP TABLE IF EXISTS ticket.drink;

DROP TABLE IF EXISTS ticket.receipt;

DROP TABLE IF EXISTS ticket.ticket_reservation_concert_day;

DROP TABLE IF EXISTS ticket.concert_day;

DROP TABLE IF EXISTS ticket.concert_day_reservation;

DROP TABLE IF EXISTS ticket.audit_log;

CREATE TABLE IF NOT EXISTS ticket.car_parking
(
    id             UUID,
    parking_number BIGINT    NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ticket.parking_reservation
(
    id             UUID      NOT NULL,
    reference      UUID      NOT NULL UNIQUE,
    car_parking_id UUID,
    created_at     TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (car_parking_id)
        REFERENCES ticket.car_parking (id)
);

CREATE TABLE IF NOT EXISTS ticket.concert_day
(
    id           UUID         NOT NULL,
    name         VARCHAR(255) NULL,
    description  VARCHAR(255) NULL,
    concert_date TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ticket.concert_day_reservation
(
    id         UUID      NOT NULL UNIQUE,
    reference  UUID      NOT NULL,
    concert_id UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (concert_id)
        REFERENCES ticket.concert_day (id)
);

CREATE TABLE IF NOT EXISTS ticket.ticket_reservation
(
    id                     UUID      NOT NULL,
    reference              UUID      NOT NULL UNIQUE,
    name                   varchar,
    address                varchar,
    birth_date             date,
    parking_reservation_id UUID      NULL,
    created_at             TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (parking_reservation_id)
        REFERENCES ticket.parking_reservation (id)
);

CREATE TABLE IF NOT EXISTS ticket.drink
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

CREATE TABLE IF NOT EXISTS ticket.meal
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

CREATE TABLE IF NOT EXISTS ticket.drink_reservation
(
    id                    UUID,
    reference             UUID      NOT NULL UNIQUE,
    ticket_reservation_id UUID      NOT NULL,
    drink_id              UUID      NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (ticket_reservation_id)
        REFERENCES ticket.ticket_reservation (id),
    FOREIGN KEY (drink_id)
        REFERENCES ticket.drink (id)
);

CREATE TABLE IF NOT EXISTS ticket.meal_reservation
(
    id                    UUID,
    reference             UUID      NOT NULL UNIQUE,
    coupon                UUID      NULL,
    ticket_reservation_id UUID      NOT NULL,
    meal_id               UUID      NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (ticket_reservation_id)
        REFERENCES ticket.ticket_reservation (id),
    FOREIGN KEY (meal_id)
        REFERENCES ticket.meal (id)
);

CREATE TABLE ticket.receipt
(
    id                    UUID,
    reference             UUID,
    created_at            TIMESTAMP DEFAULT LOCALTIMESTAMP,
    ticket_reservation_id UUID NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ticket_reservation_id)
        REFERENCES ticket.ticket_reservation (id)
);

CREATE TABLE ticket.ticket_reservation_concert_day
(
    id                    UUID NOT NULL,
    ticket_reservation_id UUID,
    concert_day_id        UUID,
    FOREIGN KEY (ticket_reservation_id)
        REFERENCES ticket.ticket_reservation (id),
    FOREIGN KEY (concert_day_id)
        REFERENCES ticket.concert_day_reservation (id)
);

CREATE TABLE ticket.audit_log
(
    id             UUID NOT NULL,
    audit_log_type VARCHAR,
    payload        VARCHAR,
    created_at     TIMESTAMP DEFAULT LOCALTIMESTAMP
)
