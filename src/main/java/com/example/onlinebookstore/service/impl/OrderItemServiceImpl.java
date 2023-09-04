package com.example.onlinebookstore.service.impl;

import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.OrderItemMapper;
import com.example.onlinebookstore.model.OrderItem;
import com.example.onlinebookstore.repository.OrderItemRepository;
import com.example.onlinebookstore.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Set<OrderItemDto> getByOrderId(Long id) {
        return orderItemRepository.getOrderItemsByOrderId(id).stream()
                .map(orderItemMapper::orderItemToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemDto getById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order item with id: " + id));
        return orderItemMapper.orderItemToDto(orderItem);
    }
}
