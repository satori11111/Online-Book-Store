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
import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static CategoryDto categoryDto;
    private static CreateCategoryRequestDto requestDto;
    private static BookDto bookDto;
    private static CategoryDto expectedCategoryDto;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/book-category-insert.sql")
            );
        }
        requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Test Category");
        requestDto.setDescription("Test Description");
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");

        CategoryDto categoryDtoResponseSecond = new CategoryDto();
        categoryDtoResponseSecond.setId(1L);
        categoryDtoResponseSecond.setName("Category 1");
        categoryDtoResponseSecond.setDescription("Description for Category 1");

        expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(2L);
        expectedCategoryDto.setName("Category 2");
        expectedCategoryDto.setDescription("Description for Category 2");

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
    @DisplayName("findAllCategories_returnsList_ok")
    @Test
    @Order(7)
    void findAllCategories_returnsList_ok() {
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
                categoryDto, actualDtos.get(1), "id");
        categoryDto.setName("Test Category");
        boolean isEqualsSecond = EqualsBuilder.reflectionEquals(
                categoryDto, actualDtos.get(0), "id");
        assertTrue(isEquals);
        assertTrue(isEqualsSecond);
        categoryDto.setName("updated Name");
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("createCategory_returnsCreatedCategory_ok")
    @Test
    @Order(2)
    void createCategory_returnsCreatedCategory_ok() {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(categoryDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @DisplayName("getCategoryById_returnsCategory_ok")
    @Test
    @Order(9)
    void getCategoryById_returnsCategory_ok() {
        MvcResult result = mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(categoryDto, actual, "id");;
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("updateCategory_returnsUpdatedCategory_ok")
    @Test
    @Order(6)
    void updateCategory_returnsUpdatedCategory_ok() {
        CreateCategoryRequestDto updatedRequestDto = requestDto;
        updatedRequestDto.setName("updated Name");
        String jsonRequest = objectMapper.writeValueAsString(updatedRequestDto);
        MvcResult result = mockMvc.perform(put("/api/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        categoryDto.setName("updated Name");
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(categoryDto, actual, "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("deleteCategoryById_deletesCategory_ok")
    @Test
    @Order(1)
    void deleteCategoryById_deleteCategory_ok() {
        MvcResult result = mockMvc.perform(delete("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertThrows(EntityNotFoundException.class,
                () -> categoryRepository.findById(2L).orElseThrow(
                        () -> new EntityNotFoundException("Can't get category by id: 2")));
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(8)
    void deleteById_NonValidId_ThrowsException() {
        mockMvc.perform(delete("/api/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("updateCategory_NonValidId_throwsException_notOk")
    @Test
    @Order(5)
    void updateCategory_NonValidId_throwsException_notOk() {
        mockMvc.perform(put("/api/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @DisplayName("getCategoryById_NonValidId_throwsException_notOk")
    @Test
    @Order(3)
    void getCategoryById_NonValidId_throwsException_notOk() {
        mockMvc.perform(get("/api/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "admin")
    @DisplayName("getBooksByCategoryId_returnsMatchingBooks_ok")
    @Test
    @Order(4)
    void getBooksByCategoryId_returnsMatchingBooks_ok() {
        MvcResult result = mockMvc.perform(get("/api/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        List<BookDto> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        List<BookDto> actualDtos = objectMapper.convertValue(actual,
                new TypeReference<List<BookDto>>() {});
        int expectedSize = 1;
        assertEquals(expectedSize, actual.size());
        System.out.println(actualDtos.get(0));
        assertEquals(List.of(bookDto), actualDtos);
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db/book-category-delete.sql")
            );
        }
    }

}

