package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateQuantityCartItemDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.CartItemMapper;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.repository.book.BookRepository;
import com.example.onlinebookstore.service.CartItemService;
import com.example.onlinebookstore.service.UserService;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UpdateQuantityCartItemDto update(UpdateQuantityCartItemDto cartItemDto, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find Cart Item by id: " + id));
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toUpdateDto(cartItem);
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.getBookById(requestDto.getBookId()));
        Long id = userService.getAuthenticatedUser().getId();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find Shopping cart by id: " + id));
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public Set<CartItemDto> findByShoppingCart(Long id) {
        return cartItemRepository.findCartItemsByShoppingCartId(id).stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
    }
}
