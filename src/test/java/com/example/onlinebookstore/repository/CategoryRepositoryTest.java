package com.example.onlinebookstore.repository;

import static com.example.onlinebookstore.config.SqlFilesPaths.BOOK_CATEGORY_DELETE;
import static com.example.onlinebookstore.config.SqlFilesPaths.BOOK_CATEGORY_INSERT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.onlinebookstore.model.Book;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    private static final long CATEGORY_ID = 1L;
    @Autowired
    private CategoryRepository categoryRepository;

    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test getBooksByCategoriesId with valid request")
    @Test
    public void getBooksByCategoriesId_validId_returnsOneBook() {
        List<Book> actual = categoryRepository.getBooksByCategoriesId(1L);
        Integer expected = 1;
        assertEquals(expected,actual.size());
        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setCategories(Set.of(categoryRepository.getCategoryById(CATEGORY_ID)));
        expectedBook.setIsbn("ISBN-001");
        expectedBook.setTitle("Book 1");
        expectedBook.setAuthor("Author 1");
        expectedBook.setPrice(BigDecimal.valueOf(20));
        expectedBook.setDescription("Description for Book 1");
        expectedBook.setCoverImage("image1.jpg");
        assertEquals(expectedBook,actual.get(0));
    }
}
