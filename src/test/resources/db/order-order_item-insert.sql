
INSERT INTO orders (id,user_id, status, total, shipping_address, order_date, is_deleted)
VALUES
    (1,2, 'PROCESSING', 100.00, '123 Main St', '2023-09-18 12:00:00', false);
--     (2,2, 'DELIVERED', 150.00, '456 Elm St', '2023-09-18 14:30:00', false);

INSERT INTO book (id,title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1,'Book 1', 'Author 1', 'ISBN-001', 19.99, 'Description for Book 1', 'image1.jpg', false),
    (2,'Book 2', 'Author 2', 'ISBN-002', 29.99, 'Description for Book 2', 'image2.jpg', false);

INSERT INTO order_item (id,order_id, book_id, quantity, price, is_deleted)
VALUES
    (1,1, 1, 2, 50.00, false),
    (2,1, 2, 1, 30.00, false);
--     (3,2, 1, 3, 90.00, false);

