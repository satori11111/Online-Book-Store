package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.config.MapperConfig;
import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.model.Order;
import com.example.onlinebookstore.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public abstract class OrderMapper {
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.example.onlinebookstore.enums.Status.PROCESSING)")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    public abstract Order toOrderFromCart(ShoppingCart shoppingCart);
    public abstract OrderDto toDto(Order order);
    @AfterMapping
    public void setUserId(@MappingTarget OrderDto orderDto, Order order) {
        orderDto.setUserId(order.getUser().getId());
    }

    @AfterMapping
    public void setOrderItemsToDto(@MappingTarget OrderDto orderDto, Order order) {
        orderDto.setOrderItems(order.getOrderItems()
                .stream()
                .map(orderItemMapper::orderItemToDto)
                .collect(Collectors.toSet()));
    }
}
