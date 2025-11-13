package com.galonovoa.librest.controller;

import com.galonovoa.librest.model.CartItem;
import com.galonovoa.librest.model.Product;
import com.galonovoa.librest.service.CartService;
import com.galonovoa.librest.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        try {
            List<CartItem> items = cartService.getCart(username);
            
            // ✅ FIX: Convertir items a formato compatible sin recursión
            List<Map<String, Object>> compatibleItems = items.stream().map(item -> {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("id", item.getId());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("username", item.getUsername());
                
                // Convertir producto a formato compatible
                Product product = item.getProduct();
                itemMap.put("product", createProductResponse(product));
                
                return itemMap;
            }).collect(Collectors.toList());
            
            double total = cartService.getTotal(username);
            
            return Map.of(
                "items", compatibleItems,
                "total", total
            );
        } catch (Exception e) {
            System.out.println("❌ Error al obtener carrito: " + e.getMessage());
            return Map.of("items", List.of(), "total", 0.0);
        }
    }

    @PostMapping("/add")
    public Map<String, Object> addToCart(@RequestBody Map<String, Object> body) {
        try {
            String username = body.get("username").toString();
            Long productId = Long.parseLong(body.get("productId").toString());
            
            Product product = productService.getProducts().stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            CartItem cartItem = cartService.addProduct(username, product);
            
            // ✅ FIX: Devolver respuesta compatible
            Map<String, Object> response = new HashMap<>();
            response.put("id", cartItem.getId());
            response.put("quantity", cartItem.getQuantity());
            response.put("username", cartItem.getUsername());
            response.put("product", createProductResponse(cartItem.getProduct()));
            
            return response;
        } catch (Exception e) {
            System.out.println("❌ Error al agregar al carrito: " + e.getMessage());
            throw new RuntimeException("Error al agregar producto al carrito: " + e.getMessage());
        }
    }

    @PostMapping("/remove")
    public Map<String, Object> removeFromCart(@RequestBody Map<String, Object> body) {
        try {
            String username = body.get("username").toString();
            Long productId = Long.parseLong(body.get("productId").toString());
            
            cartService.removeProduct(username, productId);
            
            return Map.of("success", true, "message", "Producto eliminado del carrito");
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar del carrito: " + e.getMessage());
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    // ✅ FIX: Método helper para crear respuestas de producto sin recursión
    private Map<String, Object> createProductResponse(Product product) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("name", product.getName());
        productMap.put("price", product.getPrice());
        productMap.put("description", product.getDescription());
        productMap.put("image", product.getImage());
        productMap.put("rating", product.getRating());
        productMap.put("dateAdded", product.getDateAdded());
        productMap.put("sale", product.getSale());
        productMap.put("publisher", product.getPublisher());
        
        // Manejar categoría sin recursión
        if (product.getCategory() != null) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", product.getCategory().getId());
            categoryMap.put("name", product.getCategory().getName());
            categoryMap.put("description", product.getCategory().getDescription());
            productMap.put("category", categoryMap);
        } else {
            productMap.put("category", null);
        }
        
        return productMap;
    }
}