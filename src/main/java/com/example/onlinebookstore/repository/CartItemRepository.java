package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemsByShoppingCartId(Long id);
}
