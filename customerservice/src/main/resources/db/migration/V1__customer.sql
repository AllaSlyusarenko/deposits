create schema if not exists customer;

CREATE SEQUENCE if not exists customer.id_customers_sq as integer START 1 INCREMENT BY 1;

create table if not exists customer.customers
(
    id_customers    integer default nextval('customer.id_customers_sq')
                    constraint customers_pk primary key,
    bank_account_id numeric(20,0) not null,
    phone_number    varchar(11) unique not null
);

alter sequence customer.id_customers_sq owned by customer.customers.id_customers;