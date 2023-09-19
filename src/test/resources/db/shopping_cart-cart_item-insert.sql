INSERT INTO shopping_cart (user_id, is_deleted)
VALUES (2, false);

INSERT INTO cart_item (id, shopping_cart_id, quantity, book_id, is_deleted)
VALUES (1, 2, 3, 1, false);

INSERT INTO cart_item (id, shopping_cart_id, quantity, book_id, is_deleted)
VALUES (2, 2, 2, 2, false);

INSERT INTO shopping_cart_cart_item (shopping_cart_id, cart_item_id)
VALUES (2, 1);

INSERT INTO shopping_cart_cart_item (shopping_cart_id, cart_item_id)
VALUES (2, 2);
