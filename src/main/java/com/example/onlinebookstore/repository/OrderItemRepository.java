package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.OrderItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> getOrderItemsByOrderId(Long id);

    OrderItem getOrderItemByOrderIdAndId(Long orderId, Long orderItemId);
}
