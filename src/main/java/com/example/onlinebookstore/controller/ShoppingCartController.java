package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.example.onlinebookstore.service.ShoppingCartService;
import com.example.onlinebookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get user's shopping cart", description = "Get user's shopping cart")
    public ShoppingCartDto getShoppingCart() {
        Long id = userService.getAuthenticatedUser().getId();
        return shoppingCartService.getShoppingCart(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a book to the shopping cart",
            description = "Add a book to the shopping cart")
    public CartItemDto addBook(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        Long id = userService.getAuthenticatedUser().getId();
        return shoppingCartService.save(requestDto, id);
    }

    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update quantity of book",
            description = "Update quantity of book")
    public UpdateCartItemDto update(
            @RequestBody @Valid UpdateCartItemDto cartItemDto,
                                            @PathVariable Long id) {
        return shoppingCartService.update(cartItemDto, id);
    }

    @DeleteMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book from the shopping cart",
            description = "Delete a book from the shopping cart")
    public void delete(@PathVariable Long id) {
        shoppingCartService.delete(id);
    }
}
