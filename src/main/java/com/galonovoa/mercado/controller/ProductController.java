package com.galonovoa.mercado.controller;

import com.galonovoa.mercado.model.Product;
import com.galonovoa.mercado.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getProducts() {
        return service.getProducts();
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Map<String, Object> productDTO) {
        Product product = new Product(
            productDTO.get("name").toString(),
            new java.math.BigDecimal(productDTO.get("price").toString()),
            productDTO.get("description").toString(),
            productDTO.get("image").toString()
        );
        Product saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProductPartial(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates
    ) {
        Product updated = service.updateProductPartial(id, updates);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}