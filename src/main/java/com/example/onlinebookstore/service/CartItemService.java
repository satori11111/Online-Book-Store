package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateQuantityCartItemDto;
import java.util.Set;

public interface CartItemService {
    UpdateQuantityCartItemDto update(UpdateQuantityCartItemDto cartItemDto,Long id);

    void delete(Long id);

    CartItemDto save(CreateCartItemRequestDto requestDto);

    Set<CartItemDto> findByShoppingCart(Long id);
}
