package com.galonovoa.librest.repository;

import com.galonovoa.librest.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUsername(String username);

    Optional<CartItem> findByUsernameAndProductId(String username, Long productId);
}
