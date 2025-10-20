package com.galonovoa.librest.controller;

import com.galonovoa.librest.model.CartItem;
import com.galonovoa.librest.model.Product;
import com.galonovoa.librest.service.CartService;
import com.galonovoa.librest.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public Map<String, Object> getCart(@RequestParam String username) {
        List<CartItem> items = cartService.getCart(username);
        double total = cartService.getTotal(username);
        return Map.of("items", items, "total", total);
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody Map<String, Object> body) {
        String username = body.get("username").toString();
        Long productId = Long.parseLong(body.get("productId").toString());
        Product product = productService.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return cartService.addProduct(username, product);
    }

    @PostMapping("/remove")
    public void removeFromCart(@RequestBody Map<String, Object> body) {
        String username = body.get("username").toString();
        Long productId = Long.parseLong(body.get("productId").toString());
        cartService.removeProduct(username, productId);
    }
}