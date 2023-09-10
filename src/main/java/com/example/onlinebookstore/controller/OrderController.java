package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderItemDto;
import com.example.onlinebookstore.dto.order.UpdateStatusOrderDto;
import com.example.onlinebookstore.service.OrderService;
import com.example.onlinebookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing Order")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Operation(summary = "Get all user's orders", description = "Get all user's orders")
    @GetMapping
    public Set<OrderDto> findAllUsersOrders(Pageable pageable) {
        Long id = userService.getAuthenticatedUser().getId();
        return orderService.findAll(pageable, id);
    }

    @Operation(summary = "Add a new order", description = "Add a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto addOrder(CreateOrderRequestDto requestDto) {
        Long id = userService.getAuthenticatedUser().getId();
        return orderService.addOrder(requestDto, id);
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
        return orderService.getByOrderId(id);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Retrieve a specific item from a specific order")
    public OrderItemDto retrieveOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getByOrderIdAndOrderItemId(orderId, itemId);
    }
}
