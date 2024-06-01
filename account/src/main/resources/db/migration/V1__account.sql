create schema if not exists account;

CREATE SEQUENCE if not exists account.id_bank_accounts_sq as integer START 1 INCREMENT BY 1;

create table if not exists account.bank_accounts
(
    id_bank_accounts  integer default nextval('account.id_bank_accounts_sq')
                      constraint bank_accounts_pk primary key,
    num_bank_accounts numeric(20,0) unique not null,
    amount            money not null
);

alter sequence account.id_bank_accounts_sq owned by account.bank_accounts.id_bank_accounts;