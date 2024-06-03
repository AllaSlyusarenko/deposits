INSERT INTO deposit.request_statuses(id_request_status, request_status_name)
VALUES (1, 'Подтверждение заявки'),
       (2, 'Заявка подтверждена'),
       (3, 'Заявка одобрена'),
       (4, 'Заявка отклонена');

-- пример запроса
INSERT INTO deposit.requests(customer_id, code)
VALUES (1, '1234');


-- пример соед таблицы current_request_status
INSERT INTO deposit.current_request_status(request_id, request_status_id)
VALUES (1, 1),
       (1,2),
       (1,3);