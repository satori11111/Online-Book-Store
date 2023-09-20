INSERT INTO book (id,title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1,'Book 1', 'Author 1', 'ISBN-001', 19.99, 'Description for Book 1', 'image1.jpg', false),
    (2,'Book 2', 'Author 2', 'ISBN-002', 29.99, 'Description for Book 2', 'image2.jpg', false);

INSERT INTO category (id,name, description, is_deleted)
VALUES
    (1,'Category 1', 'Description for Category 1', false),
    (2,'Category 2', 'Description for Category 2', false);


INSERT INTO book_category (book_id, category_id)
VALUES
    (1, 1),
    (2, 2);
