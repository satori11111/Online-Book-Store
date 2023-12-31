package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.OrderItemMapper;
import com.example.onlinebookstore.mapper.OrderMapper;
import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.Order;
import com.example.onlinebookstore.model.OrderItem;
import com.example.onlinebookstore.model.ShoppingCart;
import com.example.onlinebookstore.repository.CartItemRepository;
import com.example.onlinebookstore.repository.OrderItemRepository;
import com.example.onlinebookstore.repository.OrderRepository;
import com.example.onlinebookstore.repository.ShoppingCartRepository;
import com.example.onlinebookstore.service.OrderService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public Set<OrderItemDto> getByOrderId(Long id) {
        return orderItemRepository.getOrderItemsByOrderId(id).stream()
                .map(orderItemMapper::orderItemToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemDto getByOrderIdAndOrderItemId(Long orderId, Long orderItemId) {
        OrderItem orderItem = orderItemRepository.getOrderItemByOrderIdAndId(orderId,orderItemId);
        return orderItemMapper.orderItemToDto(orderItem);
    }

    @Override
    public Set<OrderDto> findAll(Pageable pageable, Long id) {
        return orderRepository.findAllByUserId(id, pageable).stream()
                .map(order -> {
                    OrderDto orderDto = orderMapper.toDto(order);
                    orderDto.setOrderItems(getOrderItems(orderDto));
                    return orderDto;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public OrderDto addOrder(CreateOrderRequestDto requestDto, Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find shopping Cart by id: " + id)
        );
        Set<CartItem> cartItems = new HashSet<>(
                cartItemRepository.findCartItemsByShoppingCartId(id));
        shoppingCart.setCartItems(cartItems);
        Order order = orderMapper.toOrderFromCart(shoppingCart);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(getTotalPrice(order.getOrderItems()));
        Order savedOrder = saveOrderAndOrderItemsToDb(cartItems, order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto changeStatus(Long id, UpdateStatusOrderDto updateStatusDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id: " + id));
        order.setStatus(updateStatusDto.getStatus());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    private BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order saveOrderAndOrderItemsToDb(Set<CartItem> cartItems, Order order) {
        Set<OrderItem> orderItems = cartItems.stream()
                .map(orderItemMapper::cartItemToOrderItem)
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(orderItem -> orderItem.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);
        return savedOrder;
    }

    private Set<OrderItemDto> getOrderItems(OrderDto orderDto) {
        return orderItemRepository
                .getOrderItemsByOrderId(orderDto.getId()).stream()
                .map(orderItemMapper::orderItemToDto)
                .collect(Collectors.toSet());
    }
}
