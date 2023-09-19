package com.example.onlinebookstore.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
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
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static UpdateCartItemDto updateCartItemDto;
    private static CreateCartItemRequestDto invalidRequestDto;
    private static CreateCartItemRequestDto requestDto;
    private static CartItemDto cartItemDto3;
    private static CartItemDto cartItemDto2;
    private static CartItemDto cartItemDto;
    private static ShoppingCartDto shoppingCartDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        cartItemDto = new CartItemDto();
        cartItemDto.setId(2L);
        cartItemDto.setTitle("Book 2");
        cartItemDto.setQuantity(2);
        cartItemDto.setBookId(2L);

        cartItemDto2 = new CartItemDto();
        cartItemDto2.setId(1L);
        cartItemDto2.setTitle("Book 1");
        cartItemDto2.setQuantity(3);
        cartItemDto2.setBookId(1L);

        cartItemDto3 = new CartItemDto();
        cartItemDto3.setId(3L);
        cartItemDto3.setTitle("Book 1");
        cartItemDto3.setQuantity(5);
        cartItemDto3.setBookId(1L);

        requestDto = new CreateCartItemRequestDto();
        requestDto.setQuantity(5);
        requestDto.setBookId(1L);

        invalidRequestDto = new CreateCartItemRequestDto();
        invalidRequestDto.setQuantity(-3);
        invalidRequestDto.setBookId(1L);

        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(2L);
        shoppingCartDto.setUserId(2L);
        shoppingCartDto.setCartItems(Set.of(cartItemDto2, cartItemDto));

        updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setQuantity(10);
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void deleteById_validId_deleteCartItem() {
        mockMvc.perform(delete("/api/cart/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void deleteById_nonValidId_throwsException() {
        mockMvc.perform(delete("/api/cart/cart-items/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void update_nonValidId_throwsException() {
        mockMvc.perform(put("/api/cart/cart-items/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void update_validId_returnsUpdatedCartItem() {
        String jsonRequest = objectMapper.writeValueAsString(updateCartItemDto);
        MvcResult result = mockMvc.perform(put("/api/cart/cart-items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        UpdateCartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UpdateCartItemDto.class);
        int expectedQuantity = 10;
        assertEquals(expectedQuantity, actual.getQuantity());

    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void save_validCartItem_returnsSavedCartItem() {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CartItemDto.class);
        assertEquals(cartItemDto3, actual);
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void save_invalidCartItem_ThrowsException() {
        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDto);
        MvcResult result = mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        String actual = result.getResponse().getContentAsString();
        assertTrue(actual.contains("quantity must be greater than or equal to 0"));
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/book-category-insert.sql",
            "classpath:db/user-role-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping_cart-cart_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/book-category-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getShoppingCart_validId_returnsShoppingCart() {
        MvcResult result = mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartDto.class);
        assertEquals(shoppingCartDto, actual);

    }
}
