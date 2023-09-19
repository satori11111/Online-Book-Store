package com.example.onlinebookstore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.shoppingcart.CartItemDto;
import com.example.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.dto.shoppingcart.UpdateCartItemDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.CartItemMapper;
import com.example.onlinebookstore.mapper.ShoppingCartMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.repository.book.BookRepository;
import com.example.onlinebookstore.service.impl.ShoppingCartServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static UpdateCartItemDto cartItemDto;
    private static ShoppingCartDto shoppingCartDto;
    private static CartItem cartItem;
    private static CartItemDto cartItemDto1;
    private static Book book;
    private static CreateCartItemRequestDto requestDto;
    private static ShoppingCart shoppingCart;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        cartItemDto = new UpdateCartItemDto();
        cartItemDto.setQuantity(3);

        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(Collections.singleton(cartItemDto1));

        cartItemDto1 = new CartItemDto();
        cartItemDto1.setId(1L);
        cartItemDto1.setTitle("Sample Book 1");
        cartItemDto1.setQuantity(3);
        cartItemDto1.setBookId(101L);

        cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setId(cartItemDto1.getId());
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemDto1.getQuantity());

        CartItemDto cartItemDto2 = new CartItemDto();
        cartItemDto2.setId(2L);
        cartItemDto2.setTitle("Sample Book 2");
        cartItemDto2.setQuantity(2);
        cartItemDto2.setBookId(102L);
        requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(2L);
        requestDto.setQuantity(3);

        book = new Book();
        book.setId(requestDto.getBookId());

        Long shoppingCartId = 1L;
        shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartId);
    }

    @Test
    public void update_validCartItem_returnsUpdatedQuantity() {
        Long id = 1L;

        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setQuantity(2);

        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(cartItemMapper.toUpdateDto(any(CartItem.class))).thenReturn(cartItemDto);

        UpdateCartItemDto updatedCartItemDto = shoppingCartService.update(cartItemDto, id);

        assertEquals(cartItemDto.getQuantity(), updatedCartItemDto.getQuantity());
        verify(cartItemRepository, times(1)).findById(anyLong());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartItemMapper, times(1)).toUpdateDto(any(CartItem.class));
    }

    @Test
    public void deleteById_validId_deleteCartItem() {
        Long id = 1L;
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));

        assertDoesNotThrow(() -> shoppingCartService.delete(id));
        verify(cartItemRepository, times(1)).findById(id);
        verify(cartItemRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteById_nonValidId_throwsException() {
        Long id = 1L;
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> shoppingCartService.delete(id));
        String expected = "Can't find Cart Item by id: 1";
        assertEquals(expected, exception.getMessage());
        verify(cartItemRepository, times(1)).findById(anyLong());
        verify(cartItemRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void update_nonValidId_throwsException() {
        Long id = 1L;
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> shoppingCartService.update(cartItemDto,id));
        String expected = "Can't find Cart Item by id: 1";
        assertEquals(expected, exception.getMessage());
        verify(cartItemRepository, times(1)).findById(anyLong());
        verify(cartItemRepository, times(0)).save(any(CartItem.class));
    }

    @Test
    public void getShoppingCart_nonValidId_throwsException() {
        Long id = 1L;
        when(shoppingCartRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> shoppingCartService.getShoppingCart(1L));
        String expected = "Can't find shopping cart by id: 1";
        assertEquals(expected, exception.getMessage());
        verify(shoppingCartRepository, times(1)).findById(anyLong());
    }

    @Test
    public void save_validCartItem_returnsSavedCartItem() {
        when(bookRepository.getBookById(requestDto.getBookId())).thenReturn(book);
        when(shoppingCartRepository.findById(shoppingCart.getId()))
                .thenReturn(Optional.of(shoppingCart));
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        when(cartItemMapper.toDto(any())).thenReturn(cartItemDto);

        CartItemDto savedCartItemDto = shoppingCartService.save(requestDto, shoppingCart.getId());

        assertEquals(cartItemDto, savedCartItemDto);
        verify(bookRepository, times(1)).getBookById(requestDto.getBookId());
        verify(shoppingCartRepository, times(1)).findById(shoppingCart.getId());
        verify(cartItemRepository, times(1)).save(any());
    }

    @Test
    public void findByShoppingCart_validRequest_returnsSetOfCartItem() {
        Long shoppingCartId = 1L;

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        when(cartItemRepository.findCartItemsByShoppingCartId(shoppingCartId))
                .thenReturn(cartItems);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(cartItemDto1);
        assertEquals(Collections.singleton(cartItemDto1),
                shoppingCartService.findByShoppingCart(1L));
        verify(cartItemRepository, times(1))
                .findCartItemsByShoppingCartId(shoppingCartId);
    }

    @Test
    public void getShoppingCart_validId_returnsShoppingCart() {
        Long shoppingCartId = 1L;
        when(shoppingCartRepository.findById(shoppingCartId)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result = shoppingCartService.getShoppingCart(shoppingCartId);
        assertNotNull(result);
        assertEquals(shoppingCartId, result.getUserId());
        verify(shoppingCartRepository, times(1)).findById(shoppingCartId);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }
}
