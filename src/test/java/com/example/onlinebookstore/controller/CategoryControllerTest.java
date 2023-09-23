package com.example.onlinebookstore.controller;

import static com.example.onlinebookstore.config.SqlFilesPaths.BOOK_CATEGORY_DELETE;
import static com.example.onlinebookstore.config.SqlFilesPaths.BOOK_CATEGORY_INSERT;
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
import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static CategoryDto expectedCategoryResponseDto;
    private static CreateCategoryRequestDto requestDto;
    private static BookDto bookDto;
    private static CategoryDto expectedCategoryDto2;
    private static CategoryDto expectedCategoryDto;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Test Category");
        requestDto.setDescription("Test Description");
        expectedCategoryResponseDto = new CategoryDto();
        expectedCategoryResponseDto.setId(1L);
        expectedCategoryResponseDto.setName("updated Name");
        expectedCategoryResponseDto.setDescription("Test Description");

        expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(1L);
        expectedCategoryDto.setName("Category 1");
        expectedCategoryDto.setDescription("Description for Category 1");

        expectedCategoryDto2 = new CategoryDto();
        expectedCategoryDto2.setId(2L);
        expectedCategoryDto2.setName("Category 2");
        expectedCategoryDto2.setDescription("Description for Category 2");

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Book 1");
        bookDto.setAuthor("Author 1");
        bookDto.setIsbn("ISBN-001");
        bookDto.setPrice(BigDecimal.valueOf(20));
        bookDto.setDescription("Description for Book 1");
        bookDto.setCoverImage("image1.jpg");
        bookDto.setCategoriesIds(null);

    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test findAll with valid request")
    @Test
    void findAllCategories_validRequest_returnsList() {
        MvcResult result = mockMvc.perform(get("/api/categories?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        List<CategoryDto> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        List<CategoryDto> actualDtos = objectMapper.convertValue(actual,
                new TypeReference<List<CategoryDto>>() {});
        int expectedSize = 2;
        assertEquals(expectedSize, actual.size());
        boolean isEquals = EqualsBuilder.reflectionEquals(
                expectedCategoryDto, actualDtos.get(1), "id");
        boolean isEqualsSecond = EqualsBuilder.reflectionEquals(
                expectedCategoryDto2, actualDtos.get(0), "id");
        assertTrue(isEquals);
        assertTrue(isEqualsSecond);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test create with valid request")
    @Test
    void createCategory_validCategory_returnsCreatedCategory() {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(
                expectedCategoryResponseDto, actual, "id");
        System.out.println(expectedCategoryResponseDto);
        System.out.println(actual);
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test getById with valid request")
    @Test
    void getById_validId_returnsCategory() {
        MvcResult result = mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(expectedCategoryDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test update with valid request")
    @Test
    void updateCategory_validCategory_returnsUpdatedCategory() {
        CreateCategoryRequestDto updatedRequestDto = requestDto;
        updatedRequestDto.setName("updated Name");
        String jsonRequest = objectMapper.writeValueAsString(updatedRequestDto);
        MvcResult result = mockMvc.perform(put("/api/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(
                expectedCategoryResponseDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test delete with valid request")
    @Test
    void deleteCategoryById_validId_deleteCategory() {
        mockMvc.perform(delete("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertThrows(EntityNotFoundException.class,
                () -> categoryRepository.findById(2L).orElseThrow(
                        () -> new EntityNotFoundException("Can't get category by id: 2")));
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test deleteById with non valid request")
    @Test
    void deleteById_nonValidId_throwsException() {
        mockMvc.perform(delete("/api/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test update with non valid request")
    @Test
    void updateCategory_nonValidId_throwsException() {
        mockMvc.perform(put("/api/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test findAll with non valid request")
    @Test
    void getCategoryById_nonValidId_throwsException_notOk() {
        mockMvc.perform(get("/api/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @Sql(scripts = {
            BOOK_CATEGORY_INSERT
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            BOOK_CATEGORY_DELETE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test getById with valid request")
    @Test
    void getBooksByCategoryId_validId_returnsBook() {
        MvcResult result = mockMvc.perform(get("/api/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        List<BookDto> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        List<BookDto> actualDtos = objectMapper.convertValue(actual,
                new TypeReference<List<BookDto>>() {});
        int expectedSize = 1;
        assertEquals(expectedSize, actual.size());
        assertEquals(List.of(bookDto), actualDtos);
    }
}

