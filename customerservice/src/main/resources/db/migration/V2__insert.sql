-- 2.2
INSERT INTO customer.customers(id_customers, phone_number)
VALUES (1, '79161234567'),
       (2, '79269876543'),
       (3, '79361234567');

-- 2.16
INSERT INTO customer.enter_code(id_customer, code)
VALUES (1, '1234'),
       (1, '7916'),
       (2, '4561'),
       (2, '3784');

-- 2.18
INSERT INTO customer.bank_account_customer(id_bank_account_customer, customer_id, bank_account_id)
VALUES (1, 1, 1),
       (2, 2, 2);