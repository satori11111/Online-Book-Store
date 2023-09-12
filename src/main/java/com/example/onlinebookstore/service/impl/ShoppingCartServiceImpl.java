package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.service.CartItemService;
import com.example.onlinebookstore.service.ShoppingCartService;
import com.example.onlinebookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final CartItemService cartItemService;

    @Override
    public ShoppingCartDto getShoppingCart() {
        User authenticatedUser = userService.getAuthenticatedUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart ny id: " + authenticatedUser.getId()));
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        Long id = shoppingCart.getId();
        shoppingCartDto.setId(id);
        shoppingCartDto.setCartItems(cartItemService.findByShoppingCart(id));
        return shoppingCartDto;
    }

    @Override
    public CartItemDto addBook(CreateCartItemRequestDto createCartItemRequestDto) {
        return cartItemService.save(createCartItemRequestDto);
    }
}
