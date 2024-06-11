CREATE SCHEMA IF NOT EXISTS deposit;

-- 2.8

CREATE SEQUENCE if not exists deposit.id_type_percent_payment_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.types_percent_payment
(
    id_type_percent_payment     integer default nextval('deposit.id_type_percent_payment_sq')
                                constraint types_percent_payment_pk primary key,
    type_percent_payment_period varchar(13) not null
);

alter sequence deposit.id_type_percent_payment_sq owned by deposit.types_percent_payment.id_type_percent_payment;

-- 2.15

-- CREATE SEQUENCE if not exists deposit.id_request_types_percent_payment_sq as integer START 1 INCREMENT BY 1;
--
-- create table if not exists deposit.request_types_percent_payment
-- (
--     id_type_percent_payment     integer default nextval('deposit.id_request_types_percent_payment_sq')
--                                 constraint request_types_percent_payment_pk primary key,
--     type_percent_payment_period varchar(13) not null
-- );
--
-- alter sequence deposit.id_request_types_percent_payment_sq owned by deposit.request_types_percent_payment.id_type_percent_payment;

-- 2.7

CREATE SEQUENCE if not exists deposit.id_deposit_type_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposits_types
(
    id_deposits_types     integer default nextval('deposit.id_deposit_type_sq')
                          constraint deposits_types_pk primary key,
    deposits_types_name   varchar(28) not null
);

alter sequence deposit.id_deposit_type_sq owned by deposit.deposits_types.id_deposits_types;

-- 2.5

CREATE SEQUENCE if not exists deposit.id_request_status_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.request_statuses
(
    id_request_status   integer default nextval('deposit.id_request_status_sq')
                        constraint request_statuses_pk primary key,
    request_status_name varchar(21) not null
);

alter sequence deposit.id_request_status_sq owned by deposit.request_statuses.id_request_status;

-- 2.10

CREATE SEQUENCE if not exists deposit.id_deposit_rate_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposit_rate
(
    id_deposit_rate   integer default nextval('deposit.id_deposit_rate_sq')
                      constraint deposit_rate_pk primary key,
    deposit_rate      decimal(4,2) not null
);

alter sequence deposit.id_deposit_rate_sq owned by deposit.deposit_rate.id_deposit_rate;

-- 2.13

CREATE SEQUENCE if not exists deposit.id_deposit_term_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposit_term
(
    id_deposit_term   integer default nextval('deposit.id_deposit_term_sq')
                      constraint deposit_term_pk primary key,
    deposit_term_name varchar(6) not null
);

alter sequence deposit.id_deposit_term_sq owned by deposit.deposit_term.id_deposit_term;

-- 2.14
--
-- CREATE SEQUENCE if not exists deposit.id_request_term_sq as integer START 1 INCREMENT BY 1;
--
-- create table if not exists deposit.request_term
-- (
--     id_request_term   integer default nextval('deposit.id_request_term_sq')
--                       constraint request_term_pk primary key,
--     request_term_name varchar(6) not null
-- );
--
-- alter sequence deposit.id_request_term_sq owned by deposit.request_term.id_request_term;


-- 2.3

CREATE SEQUENCE if not exists deposit.id_request_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.requests
(
    id_request               integer default nextval('deposit.id_request_sq')
                             constraint requests_pk primary key,
    customer_id              integer not null,
    request_date_time        timestamp with time zone default CURRENT_TIMESTAMP not null,
--     code                     varchar(4),
--     code_date_time           timestamp with time zone,
    is_deposit_refill        boolean not null,
    is_reduction_of_deposit  boolean not null,
    id_deposit_term          integer not null REFERENCES deposit.deposit_term (id_deposit_term),
    deposit_amount           numeric(20,2) default 10000 not null,
    currency                 varchar(15) default 'RUR',
    id_type_percent_payment  integer not null REFERENCES deposit.types_percent_payment (id_type_percent_payment),
    percent_payment_account_id   numeric(20,0) not null,
    deposit_refund_account_id    numeric(20,0) not null,
    deposit_debiting_account_id  numeric(20,0) not null
);

alter sequence deposit.id_request_sq owned by deposit.requests.id_request;

-- 2.4

create table if not exists deposit.current_request_status
(
    request_id          integer not null REFERENCES deposit.requests (id_request),
    request_status_id   integer not null REFERENCES deposit.request_statuses (id_request_status),
    change_datetime     timestamp with time zone default CURRENT_TIMESTAMP,
    CONSTRAINT id_current_request_status_pk PRIMARY KEY (request_id, request_status_id)
);

-- 2.12

CREATE SEQUENCE if not exists deposit.id_deposit_status_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposit_statuses
(
    id_deposit_status   integer default nextval('deposit.id_deposit_status_sq')
                        constraint deposit_statuses_pk primary key,
    deposit_status_name varchar(32) not null
);

alter sequence deposit.id_deposit_status_sq owned by deposit.deposit_statuses.id_deposit_status;

-- 2.6
CREATE SEQUENCE if not exists deposit.id_deposit_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposits
(
    id_deposit                   integer default nextval('deposit.id_deposit_sq')
                                 constraint deposits_pk primary key,
    id_request                   integer not null REFERENCES deposit.requests (id_request),
    customer_id                  integer not null,
    is_deposit_refill            boolean not null,
    is_reduction_of_deposit      boolean not null,
    id_deposits_types            integer not null REFERENCES deposit.deposits_types (id_deposits_types),
    start_date                   timestamp with time zone default CURRENT_TIMESTAMP not null,
    id_deposit_term              integer not null REFERENCES deposit.deposit_term (id_deposit_term),
    end_date                     timestamp with time zone not null,
    deposit_amount               money default 10000 not null,
    id_deposit_rate              integer not null REFERENCES deposit.deposit_rate (id_deposit_rate),
    id_type_percent_payment      integer not null REFERENCES deposit.types_percent_payment (id_type_percent_payment),
    deposit_account_id           numeric(20,0) not null,
    deposit_debiting_account_id  numeric(20,0) not null,
    percent_payment_account_id   numeric(20,0) not null,
    deposit_refund_account_id    numeric(20,0) not null,
    is_active                    boolean not null
);

alter sequence deposit.id_deposit_sq owned by deposit.deposits.id_deposit;


-- 2.11

create table if not exists deposit.current_deposit_status
(
    deposit_id          integer not null REFERENCES deposit.deposits (id_deposit),
    deposit_status_id   integer not null REFERENCES deposit.deposit_statuses (id_deposit_status),
    change_datetime     timestamp with time zone default CURRENT_TIMESTAMP,
    CONSTRAINT id_current_deposit_status_pk PRIMARY KEY (deposit_id, deposit_status_id)
);

-- 2.17

CREATE SEQUENCE if not exists deposit.id_request_code_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.request_code
(
    id_request_code   integer default nextval('deposit.id_request_code_sq')
                      constraint enter_code_pk primary key,
    id_request        integer,
    code              varchar(4),
    code_date_time    timestamp with time zone default CURRENT_TIMESTAMP
);

alter sequence deposit.id_request_code_sq owned by deposit.request_code.id_request_code;