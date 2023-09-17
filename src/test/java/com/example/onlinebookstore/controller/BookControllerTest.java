package com.example.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.repository.book.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static CreateBookRequestDto createBookRequest;
    private static BookDto expectedBookDto;
    private static BookDto expectedCreatedBookDto;
    private static BookDto expectedBookDto2;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setTitle("Book 1");
        expectedBookDto.setAuthor("Author 1");
        expectedBookDto.setIsbn("ISBN-001");
        expectedBookDto.setPrice(BigDecimal.valueOf(20));
        expectedBookDto.setDescription("Description for Book 1");
        expectedBookDto.setCoverImage("image1.jpg");
        expectedBookDto.setCategoriesIds(Set.of(1L));

        expectedBookDto2 = new BookDto();
        expectedBookDto2.setId(2L);
        expectedBookDto2.setTitle("Book 2");
        expectedBookDto2.setAuthor("Author 2");
        expectedBookDto2.setIsbn("ISBN-002");
        expectedBookDto2.setPrice(BigDecimal.valueOf(30));
        expectedBookDto2.setDescription("Description for Book 2");
        expectedBookDto2.setCoverImage("image2.jpg");
        expectedBookDto2.setCategoriesIds(Set.of(2L));

        createBookRequest = new CreateBookRequestDto();
        createBookRequest.setTitle("Pride and Prejudice");
        createBookRequest.setAuthor("Jane Austen");
        createBookRequest.setIsbn("978-1-60309-515-0");
        createBookRequest.setPrice(new BigDecimal(13));
        createBookRequest.setDescription("A classic romance novel");
        createBookRequest.setCoverImage("https://pride.com/prejudice.jpg");
        Set<Long> categoryIdsForDto = new HashSet<>();
        categoryIdsForDto.add(2L);
        createBookRequest.setCategoryIds(categoryIdsForDto);

        expectedCreatedBookDto = new BookDto();
        expectedCreatedBookDto.setTitle(createBookRequest.getTitle());
        expectedCreatedBookDto.setAuthor(createBookRequest.getAuthor());
        expectedCreatedBookDto.setIsbn(createBookRequest.getIsbn());
        expectedCreatedBookDto.setCoverImage(createBookRequest.getCoverImage());
        expectedCreatedBookDto.setDescription(createBookRequest.getDescription());
        expectedCreatedBookDto.setCategoriesIds(categoryIdsForDto);
        expectedCreatedBookDto.setPrice(createBookRequest.getPrice());
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test findAll with valid request")
    @Test
    void findAll_validRequest_returnsList() {
        MvcResult result = mockMvc.perform(get("/api/books?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        List<BookDto> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        List<BookDto> actualDtos = objectMapper.convertValue(actual,
                new TypeReference<List<BookDto>>() {
                });
        int expectedSize = 2;
        assertEquals(expectedSize, actual.size());

        boolean isEquals = EqualsBuilder.reflectionEquals(
                expectedBookDto, actualDtos.get(0), "id");
        boolean isEqualsSecond = EqualsBuilder.reflectionEquals(
                expectedBookDto2, actualDtos.get(1), "id");
        assertTrue(isEquals);
        assertTrue(isEqualsSecond);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test create with valid request")
    @Test
    void createBook_validBook_returnsCreatedBook() {
        String jsonRequest = objectMapper.writeValueAsString(createBookRequest);
        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(expectedCreatedBookDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test getById with valid request")
    @Test
    void getById_validId_returnsBook() {
        MvcResult result = mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(expectedBookDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})

    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test update with valid request")
    @Test
    void updateBook_validBook_returnsUpdatedBook() {
        CreateBookRequestDto updatedRequestDto = createBookRequest;
        updatedRequestDto.setIsbn("978-1-60309-527-3");
        String jsonRequest = objectMapper.writeValueAsString(updatedRequestDto);
        MvcResult result = mockMvc.perform(put("/api/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        expectedCreatedBookDto.setIsbn(createBookRequest.getIsbn());
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(expectedCreatedBookDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test deleteById with valid request")
    @Test
    void deleteById_validId_deleteBook() {
        mockMvc.perform(delete("/api/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertThrows(EntityNotFoundException.class,
                () -> bookRepository.findByIdBookAndItsCategories(2L).orElseThrow(
                        () -> new EntityNotFoundException("Can't get book by id: 2")));
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test deleteById with non valid request")
    @Test
    void deleteById_NonValidId_throwsException() {
        mockMvc.perform(delete("/api/books/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test update with non valid request")
    @Test
    void update_nonValidId_throwsException() {
        mockMvc.perform(put("/api/books/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test getBuId with non valid request")
    @Test
    void getById_nonValidId_throwsException() {
        mockMvc.perform(get("/api/books/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test search with valid request")
    @Test
    void search_validSearchParams_returnsMatchingBooks() {
        MvcResult result = mockMvc.perform(get("/api/books/search?authors=Author 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        List<BookDto> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        int expectedSize = 1;
        assertEquals(expectedSize, actual.size());
        EqualsBuilder.reflectionEquals(expectedCreatedBookDto, actual.get(0), "id");
    }
}
