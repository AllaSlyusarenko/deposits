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

-- 2.7

CREATE SEQUENCE if not exists deposit.id_deposit_type_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposits_types
(
    id_deposits_types     integer default nextval('deposit.id_deposit_type_sq')
                        constraint deposits_types_pk primary key,
    deposits_types_name   varchar(13) not null
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

-- 2.3

CREATE SEQUENCE if not exists deposit.id_request_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.requests
(
    id_request        integer default nextval('deposit.id_request_sq')
                      constraint requests_pk primary key,
    customer_id       integer not null,
    request_date_time timestamp with time zone default CURRENT_TIMESTAMP not null,
    code              varchar(4),
    code_date_time    timestamp with time zone,
    is_deposit_refill       boolean not null,
    is_reduction_of_deposit boolean not null,
    id_deposit_term   integer not null REFERENCES deposit.deposit_term (id_deposit_term),
    deposit_amount    money default 10000 not null,
    id_type_percent_payment integer not null REFERENCES deposit.types_percent_payment (id_type_percent_payment),
    percent_payment_account_id numeric(20,0) not null,
    deposit_refund_account_id  numeric(20,0) not null,
    deposit_debiting_account_id  numeric(20,0) not null
--     CHECK (deposit_amount >= 10000::money)
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

-- 2.6

-- 2.11