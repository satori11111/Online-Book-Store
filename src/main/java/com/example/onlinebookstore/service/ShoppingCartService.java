package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import java.util.Set;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long id);

    UpdateCartItemDto update(UpdateCartItemDto cartItemDto, Long id);

    void delete(Long id);

    CartItemDto save(CreateCartItemRequestDto requestDto, Long id);

    Set<CartItemDto> findByShoppingCart(Long id);

}
