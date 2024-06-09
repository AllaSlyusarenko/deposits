create schema if not exists customer;

-- 2.2

CREATE SEQUENCE if not exists customer.id_customers_sq as integer START 1 INCREMENT BY 1;

create table if not exists customer.customers
(
    id_customers    integer default nextval('customer.id_customers_sq')
                    constraint customers_pk primary key,
    phone_number    varchar(11) unique not null
);

alter sequence customer.id_customers_sq owned by customer.customers.id_customers;

-- 2.16

CREATE SEQUENCE if not exists customer.id_enter_code_sq as integer START 1 INCREMENT BY 1;

create table if not exists customer.enter_code
(
    id_enter_code   integer default nextval('customer.id_enter_code_sq')
                    constraint enter_code_pk primary key,
    id_customer     integer,
    code            varchar(4),
    code_date_time  timestamp with time zone default CURRENT_TIMESTAMP
);

alter sequence customer.id_enter_code_sq owned by customer.enter_code.id_customer;

-- 2.18

CREATE SEQUENCE if not exists customer.id_bank_account_customer_sq as integer START 1 INCREMENT BY 1;

create table if not exists customer.bank_account_customer
(
    id_bank_account_customer   integer default nextval('customer.id_bank_account_customer_sq')
                               constraint bank_account_customer_pk primary key,
    customer_id                integer not null,
    bank_account_id            integer not null
);

alter sequence customer.id_bank_account_customer_sq owned by customer.bank_account_customer.id_bank_account_customer;