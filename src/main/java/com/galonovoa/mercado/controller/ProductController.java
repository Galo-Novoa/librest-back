package com.galonovoa.mercado.controller;

import com.galonovoa.mercado.model.Product;
import com.galonovoa.mercado.dto.ProductDTO;
import com.galonovoa.mercado.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

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
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
        Product product = new Product(
            productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getDescription(),
            productDTO.getImage()
        );
        Product saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductDTO updatesDTO
    ) {
        Product updated = service.updateProduct(id, updatesDTO);
        return ResponseEntity.ok(updated);
    }
}