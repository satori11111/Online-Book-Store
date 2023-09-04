package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemsByOrderId(Long id);
}
