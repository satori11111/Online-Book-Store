package com.example.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import com.example.onlinebookstore.enums.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
public class OrderControllerTest {
    protected static MockMvc mockMvc;
    private static OrderItemDto orderItemDto3;
    private static OrderDto createdOrderDto;
    private static OrderDto updatedOrderDto;
    private static CreateOrderRequestDto createOrderRequestDto;
    private static UpdateStatusOrderDto statusOrderDto;
    private static OrderDto orderDto;
    private static OrderItemDto orderItemDto;
    private static OrderItemDto orderItemDto2;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        orderItemDto = new OrderItemDto();
        orderItemDto.setId(2L);
        orderItemDto.setBookId(2L);
        orderItemDto.setQuantity(1);

        orderItemDto2 = new OrderItemDto();
        orderItemDto2.setId(1L);
        orderItemDto2.setBookId(1L);
        orderItemDto2.setQuantity(2);

        orderItemDto3 = new OrderItemDto();
        orderItemDto3.setId(3L);
        orderItemDto3.setBookId(1L);
        orderItemDto3.setQuantity(3);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setOrderItems(Set.of(orderItemDto2, orderItemDto));
        orderDto.setUserId(2L);
        orderDto.setStatus(Status.PROCESSING);
        orderDto.setTotal(BigDecimal.valueOf(100));

        createdOrderDto = new OrderDto();
        createdOrderDto.setId(2L);
        createdOrderDto.setUserId(2L);
        createdOrderDto.setStatus(Status.PROCESSING);
        createdOrderDto.setTotal(BigDecimal.valueOf(50));

        updatedOrderDto = new OrderDto();
        updatedOrderDto.setId(1L);
        updatedOrderDto.setUserId(2L);
        updatedOrderDto.setStatus(Status.SENT);
        updatedOrderDto.setTotal(BigDecimal.valueOf(100));

        createOrderRequestDto = new CreateOrderRequestDto();
        createOrderRequestDto.setShippingAddress("123 Main St1");

        statusOrderDto = new UpdateStatusOrderDto();
        statusOrderDto.setStatus(Status.SENT);
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/user-role-insert.sql",
            "classpath:db/order-order_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/order-order_item-delete.sql",
            "classpath:db/user-role-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getByOrderId_validId_returnOrderItems() {
        MvcResult result = mockMvc.perform(get("/api/orders/1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        Set<OrderItemDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), HashSet.class);
        Set<OrderItemDto> actualDtos = objectMapper.convertValue(actual,
                new TypeReference<HashSet<OrderItemDto>>() {
                });
        assertEquals(Set.of(orderItemDto, orderItemDto2), actualDtos);
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/user-role-insert.sql",
            "classpath:db/order-order_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/order-order_item-delete.sql",
            "classpath:db/user-role-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getByOrderIdAndOrderItemId_validId_returnOrderItemDto() {
        MvcResult result = mockMvc.perform(get("/api/orders/1/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        OrderItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), OrderItemDto.class);
        assertEquals(orderItemDto2, actual);
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/user-role-insert.sql",
            "classpath:db/order-order_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/order-order_item-delete.sql",
            "classpath:db/user-role-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void findAll_validUserId_returnsOrderDto() {
        MvcResult result = mockMvc.perform(get("/api/orders?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        Set<OrderItemDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashSet.class);
        Set<OrderDto> actualDtos = objectMapper.convertValue(actual,
                new TypeReference<HashSet<OrderDto>>() {
                });
        boolean equals = EqualsBuilder.reflectionEquals(orderDto, actualDtos.iterator().next(),
                "orderDate");
        assertTrue(equals);
    }

    @SneakyThrows
    @WithMockUser(username = "email@gmail.com")
    @Sql(scripts = {
            "classpath:db/user-role-insert.sql",
            "classpath:db/order-order_item-insert.sql",
            "classpath:db/shopping_cart-cart_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/order-order_item-delete.sql",
            "classpath:db/user-role-delete.sql",
            "classpath:db/shopping_cart-cart_item-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void addOrder_validRequestDto_returnsOrderDto() {
        String jsonRequest = objectMapper.writeValueAsString(createOrderRequestDto);
        MvcResult result = mockMvc.perform(post("/api/orders")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        OrderDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                OrderDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(
                createdOrderDto, actual, "orderDate", "id");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/user-role-insert.sql",
            "classpath:db/order-order_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/order-order_item-delete.sql",
            "classpath:db/user-role-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void changeStatus_validId_returnOrderDto() {
        String jsonRequest = objectMapper.writeValueAsString(statusOrderDto);
        MvcResult result = mockMvc.perform(patch("/api/orders/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        OrderDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                OrderDto.class);
        boolean isEquals = EqualsBuilder.reflectionEquals(updatedOrderDto, actual, "orderDate");
        assertTrue(isEquals);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/user-role-insert.sql",
            "classpath:db/order-order_item-insert.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/order-order_item-delete.sql",
            "classpath:db/user-role-delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void changeStatus_invalidId_throwsException() {
        String jsonRequest = objectMapper.writeValueAsString(statusOrderDto);
        mockMvc.perform(patch("/api/orders/3")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }
}
