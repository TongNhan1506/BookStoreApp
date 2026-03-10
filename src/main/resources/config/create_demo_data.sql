UPDATE import_ticket
SET created_date = '2026-01-02 08:00:00', approved_date = '2026-01-02 09:30:00'
WHERE import_ticket_id = 1;

UPDATE inventory_log
SET created_date = '2026-01-02 09:30:00'
WHERE reference_id = 1 AND action = 'Nhập hàng';

UPDATE price
SET effective_date = '2026-01-02 09:30:00'
WHERE book_id IN (SELECT book_id FROM import_ticket_detail WHERE import_ticket_id = 1);


UPDATE import_ticket
SET created_date = '2026-02-15 08:15:00', approved_date = '2026-02-15 10:00:00'
WHERE import_ticket_id = 2;

UPDATE inventory_log
SET created_date = '2026-02-15 10:00:00'
WHERE reference_id = 2 AND action = 'Nhập hàng';

UPDATE price
SET effective_date = '2026-02-15 10:00:00'
WHERE book_id IN (SELECT book_id FROM import_ticket_detail WHERE import_ticket_id = 2);




UPDATE bill SET created_date = '2026-01-05 10:15:00' WHERE bill_id = 1;
UPDATE inventory_log SET created_date = '2026-01-05 10:15:00' WHERE reference_id = 1 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-01-12 14:20:00' WHERE bill_id = 2;
UPDATE inventory_log SET created_date = '2026-01-12 14:20:00' WHERE reference_id = 2 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-01-20 09:45:00' WHERE bill_id = 3;
UPDATE inventory_log SET created_date = '2026-01-20 09:45:00' WHERE reference_id = 3 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-02-02 16:30:00' WHERE bill_id = 4;
UPDATE inventory_log SET created_date = '2026-02-02 16:30:00' WHERE reference_id = 4 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-02-14 11:10:00' WHERE bill_id = 5;
UPDATE inventory_log SET created_date = '2026-02-14 11:10:00' WHERE reference_id = 5 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-02-25 15:50:00' WHERE bill_id = 6;
UPDATE inventory_log SET created_date = '2026-02-25 15:50:00' WHERE reference_id = 6 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-03-01 08:30:00' WHERE bill_id = 7;
UPDATE inventory_log SET created_date = '2026-03-01 08:30:00' WHERE reference_id = 7 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-03-05 19:20:00' WHERE bill_id = 8;
UPDATE inventory_log SET created_date = '2026-03-05 19:20:00' WHERE reference_id = 8 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-03-08 12:00:00' WHERE bill_id = 9;
UPDATE inventory_log SET created_date = '2026-03-08 12:00:00' WHERE reference_id = 9 AND action = 'Bán hàng';

UPDATE bill SET created_date = '2026-03-09 17:45:00' WHERE bill_id = 10;
UPDATE inventory_log SET created_date = '2026-03-09 17:45:00' WHERE reference_id = 10 AND action = 'Bán hàng';
