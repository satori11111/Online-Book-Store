package com.example.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private Book expectedBook2;
    private Book expectedBook;

    @BeforeEach
    void setUp() {
        expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setCategories(Collections.singleton(categoryRepository.getCategoryById(1L)));
        expectedBook.setIsbn("ISBN-001");
        expectedBook.setTitle("Book 1");
        expectedBook.setAuthor("Author 1");
        expectedBook.setPrice(BigDecimal.valueOf(20));
        expectedBook.setDescription("Description for Book 1");
        expectedBook.setCoverImage("image1.jpg");
        expectedBook2 = new Book();
        expectedBook2.setId(2L);
        expectedBook2.setTitle("Book 2");
        expectedBook2.setAuthor("Author 2");
        expectedBook2.setIsbn("ISBN-002");
        expectedBook2.setPrice(BigDecimal.valueOf(30));
        expectedBook2.setDescription("Description for Book 2");
        expectedBook2.setCoverImage("image2.jpg");
        expectedBook2.setCategories(Collections.singleton(categoryRepository.getCategoryById(2L)));
    }

    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test findByIdBookAndItsCategories with valid request")
    @Test
    public void findByIdBookAndItsCategories_validId_ReturnsOneBook() {
        Book actual = bookRepository.findByIdBookAndItsCategories(1L).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: 1"));
        Integer expected = 1;
        assertEquals(expectedBook,actual);
    }

    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test findByIdBookAndItsCategories with  non valid request")
    @Test
    public void findByIdBookAndItsCategories_InvalidId_ThrowsException() {
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () ->
                bookRepository.findByIdBookAndItsCategories(15L)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find book with id: 15")));
        String expected = "Can't find book with id: 15";
        assertEquals(expected,actual.getMessage());
    }

    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test findAllBookAndTheirCategories with valid request")
    @Test
    public void findAllBookAndTheirCategories_validRequest_ReturnsListOfBooks() {
        Pageable pageable = Pageable.ofSize(2);
        List<Book> actual = bookRepository.findAllBooksAndTheirCategories(pageable);
        Integer expected = 2;
        assertEquals(expected, actual.size());
        assertEquals(List.of(expectedBook, expectedBook2),actual);
    }

}
