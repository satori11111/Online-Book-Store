package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.config.MapperConfig;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "orderItem.book.id")
    OrderItemDto orderItemToDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "cartItem.book.price")
    OrderItem cartItemToOrderItem(CartItem cartItem);
}
