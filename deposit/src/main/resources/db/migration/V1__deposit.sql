CREATE SCHEMA IF NOT EXISTS deposit;

--

CREATE SEQUENCE if not exists deposit.id_type_percent_payment_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.types_percent_payment
(
    id_type_percent_payment     integer default nextval('deposit.id_type_percent_payment_sq')
                                constraint types_percent_payment_pk primary key,
    type_percent_payment_period varchar(13) not null
);

alter sequence deposit.id_type_percent_payment_sq owned by deposit.types_percent_payment.id_type_percent_payment;

--

CREATE SEQUENCE if not exists deposit.id_deposit_type_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.deposits_types
(
    id_deposits_types     integer default nextval('deposit.id_deposit_type_sq')
                        constraint deposits_types_pk primary key,
    deposits_types_name   varchar(13) not null
);

alter sequence deposit.id_deposit_type_sq owned by deposit.deposits_types.id_deposits_types;

--
CREATE SEQUENCE if not exists deposit.id_request_status_sq as integer START 1 INCREMENT BY 1;

create table if not exists deposit.request_statuses
(
    id_request_status   integer default nextval('deposit.id_request_status_sq')
                        constraint request_statuses_pk primary key,
    request_status_name varchar(21) not null
);

alter sequence deposit.id_request_status_sq owned by deposit.request_statuses.id_request_status;
