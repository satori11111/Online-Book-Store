package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Set<OrderDto> findAll(Pageable pageable, Long id);

    OrderDto addOrder(CreateOrderRequestDto requestDto, Long id);

    OrderDto changeStatus(Long id, UpdateStatusOrderDto updateStatusDto);

    Set<OrderItemDto> getByOrderId(Long id);

    OrderItemDto getByOrderIdAndOrderItemId(Long orderId, Long orderItemId);
}
