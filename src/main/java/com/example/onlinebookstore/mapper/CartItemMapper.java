package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.config.MapperConfig;
import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateQuantityCartItemDto;
import com.example.onlinebookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    UpdateQuantityCartItemDto toUpdateDto(CartItem cartItem);

    CartItemDto toDto(CartItem cartItem);

    @AfterMapping
    default void setShoppingCartId(@MappingTarget CartItemDto cartDto, CartItem cartItem) {
        cartDto.setShoppingCartId(cartItem.getShoppingCart().getId());
    }

    @AfterMapping
    default void setBookId(@MappingTarget CartItemDto cartDto, CartItem cartItem) {
        cartDto.setBookId(cartItem.getBook().getId());
    }

    @AfterMapping
    default void setBookTitle(@MappingTarget CartItemDto cartDto, CartItem cartItem) {
        cartDto.setBookId(cartItem.getBook().getId());
    }
}
