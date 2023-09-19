package com.example.onlinebookstore.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import com.example.onlinebookstore.enums.Status;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.OrderItemMapper;
import com.example.onlinebookstore.mapper.OrderMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.Order;
import com.example.onlinebookstore.model.OrderItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.OrderItemRepository;
import com.example.onlinebookstore.repository.OrderRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.service.impl.OrderServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private static OrderItem orderItem;
    private static OrderDto orderDto;
    private static OrderItemDto orderItemDto;
    private static CreateOrderRequestDto createOrderRequestDto;
    private static UpdateStatusOrderDto updateStatusOrderDto;
    private static ShoppingCart shoppingCart;
    private static Order order;
    private static Set<OrderItem> orderItems;
    private static List<CartItem> cartItems;
    private static Order someSavedOrder;
    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeAll
    static void beforeAll() {
        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setBook(new Book());
        orderItem.setOrder(new Order());
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(25.0));
        orderItem.setDeleted(false);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setOrderItems(new HashSet<>()); // Initialize as needed
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setTotal(BigDecimal.ZERO); // Set an appropriate value
        orderDto.setStatus(Status.PROCESSING); // Set an appropriate value

        orderItemDto = new OrderItemDto();
        orderItemDto.setId(1L);
        orderItemDto.setBookId(1L);
        orderItemDto.setQuantity(1);

        createOrderRequestDto = new CreateOrderRequestDto();
        createOrderRequestDto.setShippingAddress("123 Main St");

        updateStatusOrderDto = new UpdateStatusOrderDto();
        updateStatusOrderDto.setStatus(Status.SENT);

        shoppingCart = new ShoppingCart();

        order = new Order();
        order.setOrderItems(Set.of(orderItem));

        orderItems = new HashSet<>();
        orderItems.add(orderItem);
        cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);
        cartItem.setBook(new Book());
        cartItem.setId(1L);
        cartItem.setShoppingCart(shoppingCart);
        cartItems.add(cartItem);
        someSavedOrder = new Order();
    }

    @Test
    public void getByOrderId_validId_returnOrderItems() {
        Long orderId = 1L;
        when(orderItemRepository.getOrderItemsByOrderId(orderId)).thenReturn(orderItems);
        when(orderItemMapper.orderItemToDto(any(OrderItem.class))).thenReturn(orderItemDto);
        Set<OrderItemDto> actual = orderService.getByOrderId(orderId);

        Assertions.assertEquals(orderItems.size(), actual.size());
        Assertions.assertEquals(Set.of(orderItemDto), actual);
        verify(orderItemRepository, times(1)).getOrderItemsByOrderId(orderId);
        verify(orderItemMapper, times(orderItems.size())).orderItemToDto(any(OrderItem.class));
    }

    @Test
    public void getByOrderIdAndOrderItemId_validId_returnOrderItemDto() {
        Long orderId = 1L;
        Long orderItemId = 2L;
        when(orderItemRepository.getOrderItemByOrderIdAndId(orderId, orderItemId))
                .thenReturn(orderItem);
        when(orderItemMapper.orderItemToDto(any(OrderItem.class))).thenReturn(orderItemDto);
        OrderItemDto result = orderService.getByOrderIdAndOrderItemId(orderId, orderItemId);

        Assertions.assertEquals(orderItemDto, result);
        verify(orderItemRepository, times(1)).getOrderItemByOrderIdAndId(orderId, orderItemId);
        verify(orderItemMapper, times(1)).orderItemToDto(any(OrderItem.class));
    }

    @Test
    public void findAll_validUserId_returnsOrderDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Long userId = 1L;
        when(orderRepository.findAllByUserId(userId, pageable))
                .thenReturn(Collections.singletonList(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);
        when(orderItemRepository.getOrderItemsByOrderId(anyLong())).thenReturn(orderItems);
        when(orderItemMapper.orderItemToDto(any(OrderItem.class))).thenReturn(orderItemDto);

        Set<OrderDto> actual = orderService.findAll(pageable, userId);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(orderDto, actual.iterator().next());
        verify(orderRepository, times(1)).findAllByUserId(anyLong(), any(Pageable.class));
        verify(orderMapper, times(1)).toDto(any(Order.class));
        verify(orderItemRepository, times(1)).getOrderItemsByOrderId(anyLong());
        verify(orderItemMapper, times(1)).orderItemToDto(any(OrderItem.class));
    }

    @Test
    public void addOrder_validRequestDto_returnsOrderDto() {
        Long cartId = 1L;
        CreateOrderRequestDto requestDto = createOrderRequestDto;
        ShoppingCart shoppingCart = OrderServiceTest.shoppingCart;
        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findCartItemsByShoppingCartId(cartId)).thenReturn(cartItems);
        when(orderMapper.toOrderFromCart(shoppingCart)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(someSavedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);
        when(orderItemMapper.cartItemToOrderItem(any(CartItem.class))).thenReturn(orderItem);
        OrderDto actual = orderService.addOrder(requestDto, cartId);

        Assertions.assertEquals(orderDto, actual);
        verify(shoppingCartRepository, times(1)).findById(cartId);
        verify(cartItemRepository, times(1)).findCartItemsByShoppingCartId(cartId);
        verify(orderMapper, times(1)).toOrderFromCart(shoppingCart);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDto(any(Order.class));
    }

    @Test
    public void changeStatus_validId_returnOrderDto() {
        Long orderId = 1L;
        UpdateStatusOrderDto updateStatusDto = updateStatusOrderDto;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(someSavedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto actual = orderService.changeStatus(orderId, updateStatusDto);
        Assertions.assertEquals(orderDto, actual);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDto(any(Order.class));
    }

    @Test
    public void changeStatus_invalidId_throwsException() {
        Long orderId = 1L;
        UpdateStatusOrderDto updateStatusDto = updateStatusOrderDto;
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderService.changeStatus(orderId, updateStatusDto));
        String expected = "Can't find order by id: 1";
        Assertions.assertEquals(expected, exception.getMessage());
        verify(orderRepository, times(1)).findById(anyLong());
    }
}

