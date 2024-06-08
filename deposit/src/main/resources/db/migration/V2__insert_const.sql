-- 2.5
INSERT INTO deposit.request_statuses(id_request_status, request_status_name)
VALUES (1, 'Подтверждение заявки'),
       (2, 'Заявка подтверждена'),
       (3, 'Заявка одобрена'),
       (4, 'Заявка отклонена');

-- 2.13
INSERT INTO deposit.deposit_term(id_deposit_term, deposit_term_name)
VALUES (1, '3 мес.'),
       (2, '6 мес.'),
       (3, '1 год.');

-- 2.14
-- INSERT INTO deposit.request_term(id_request_term, request_term_name)
-- VALUES (1, '3 мес.'),
--        (2, '6 мес.'),
--        (3, '1 год.');

-- 2.8
INSERT INTO deposit.types_percent_payment(id_type_percent_payment, type_percent_payment_period)
VALUES (1, 'Ежемесячно'),
       (2, 'В конце срока'),
       (3, 'Капитализация');

-- 2.15
-- INSERT INTO deposit.request_types_percent_payment(id_type_percent_payment, type_percent_payment_period)
-- VALUES (1, 'Ежемесячно'),
--        (2, 'В конце срока'),
--        (3, 'Капитализация');

-- 2.12
INSERT INTO deposit.deposit_statuses(id_deposit_status, deposit_status_name)
VALUES (1, 'Вклад открыт'),
       (2, 'Подтверждение пополнения вклада'),
       (3, 'Пополнение вклада подтверждено'),
       (4, 'Пополнение одобрено'),
       (5, 'Подтверждение уменьшения вклада'),
       (6, 'Уменьшение вклада подтверждено'),
       (7, 'Уменьшение одобрено'),
--        проверка суммы можно ли столько списать и остаток будет мин 10 тыс.
       (8, 'Подтверждение закрытия вклада'),
       (9, 'Закрытие вклада подтверждено'),
       (10, 'Вклад закрыт');

-- 2.10
INSERT INTO deposit.deposit_rate(id_deposit_rate, deposit_rate)
VALUES (1, 15),
       (2, 14.5),
       (3, 14),
       (4, 14),
       (5, 13.5),
       (6, 13),
       (7, 13),
       (8, 12.5),
       (9, 12),
       (10, 12),
       (11, 11.5),
       (12, 11),
       (13, 11),
       (14, 10.5),
       (15, 10);

-- пример запроса 2.3
INSERT INTO deposit.requests(customer_id, is_deposit_refill, is_reduction_of_deposit,
id_deposit_term, deposit_amount,  id_type_percent_payment, percent_payment_account_id,
 deposit_refund_account_id, deposit_debiting_account_id )
VALUES (1, true, true, 2, 5000, 1, 2345678901234567891,2345678901234567891,1234567890123456789);


-- пример соед таблицы current_request_status
INSERT INTO deposit.current_request_status(request_id, request_status_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);

-- 2.17
INSERT INTO deposit.request_code(id_request, code)
VALUES (1, '1234'),
       (1, '7916');