package com.galonovoa.mercado.service;

import com.galonovoa.mercado.model.CartItem;
import com.galonovoa.mercado.model.Product;
import com.galonovoa.mercado.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository repository;

    public CartService(CartItemRepository repository) {
        this.repository = repository;
    }

    public List<CartItem> getCart(String username) {
        return repository.findByUsername(username);
    }

    public CartItem addProduct(String username, Product product) {
        CartItem item = repository.findByUsernameAndProductId(username, product.getId())
                .orElse(new CartItem(product, 0, username));

        item.setQuantity(item.getQuantity() + 1);
        return repository.save(item);
    }

    public void removeProduct(String username, Long productId) {
        repository.findByUsernameAndProductId(username, productId)
                .ifPresent(repository::delete);
    }

    public double getTotal(String username) {
        return repository.findByUsername(username).stream()
                .mapToDouble(i -> i.getProduct().getPrice().doubleValue() * i.getQuantity())
                .sum();
    }
}