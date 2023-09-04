package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import com.example.onlinebookstore.service.OrderItemService;
import com.example.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;

@Tag(name = "Order management", description = "Endpoints for managing Order")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping
    public Set<OrderDto> findAll() {
        return orderService.findAll();
    }

    public OrderDto addOrder(CreateOrderRequestDto requestDto) {
        return orderService.addOrder(requestDto);
    }

    @Operation(summary = "Update status of order",
            description = "Update status of order(Admin only)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public OrderDto updateStatus(@PathVariable Long id,
                                 @RequestBody @Valid UpdateStatusOrderDto requestDto) {
        return orderService.changeStatus(id, requestDto);
    }

    @Operation(summary = "Get all items by order",
            description = "Get all items by order")
    @GetMapping("/{id}/items")
    public Set<OrderItemDto> getOrderItems(@PathVariable Long id) {
        return orderItemService.getByOrderId(id);
    }

}
