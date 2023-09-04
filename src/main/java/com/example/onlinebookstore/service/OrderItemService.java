package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.order.OrderItemDto;
import java.util.Set;

public interface OrderItemService {
   Set<OrderItemDto> getByOrderId(Long id);
   OrderItemDto getById(Long id);
}
