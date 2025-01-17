create schema if not exists account;

-- 2.1

CREATE SEQUENCE if not exists account.id_bank_accounts_sq as integer START 2 INCREMENT BY 1;

create table if not exists account.bank_accounts
(
    id_bank_accounts  integer default nextval('account.id_bank_accounts_sq')
                      constraint bank_accounts_pk primary key,
    num_bank_accounts numeric(20,0) unique not null,
    amount            numeric(20,2) not null,
    currency          varchar(15) default 'RUR',
    is_active         boolean not null
);

alter sequence account.id_bank_accounts_sq owned by account.bank_accounts.id_bank_accounts;