package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import org.springframework.data.domain.Pageable;
import java.util.Set;

public interface OrderService {
 Set<OrderDto> findAll();
 OrderDto addOrder(CreateOrderRequestDto requestDto);
 OrderDto changeStatus(Long id, UpdateStatusOrderDto updateStatusDto);
}
