package com.galonovoa.mercado.controller;

import com.galonovoa.mercado.model.Product;
import com.galonovoa.mercado.model.Category;
import com.galonovoa.mercado.service.ProductService;
import com.galonovoa.mercado.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private final CategoryService categoryService;

    public ProductController(ProductService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return service.getProducts();
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Map<String, Object> productDTO) {
        // Obtener la categoría si se proporciona
        Object categoryIdObj = productDTO.get("categoryId");
        Category category = null;
        if (categoryIdObj != null) {
            Long categoryId = Long.parseLong(categoryIdObj.toString());
            category = categoryService.getCategoryById(categoryId)
                    .orElse(null);
        }

        Product product = new Product();
        product.setName(productDTO.get("name").toString());
        product.setPrice(new BigDecimal(productDTO.get("price").toString()));
        product.setDescription(productDTO.get("description").toString());
        product.setImage(productDTO.get("image").toString());
        product.setRating(productDTO.containsKey("rating") ? Double.parseDouble(productDTO.get("rating").toString()) : 0.0);
        product.setPublisher(productDTO.containsKey("publisher") ? productDTO.get("publisher").toString() : "admin");
        product.setDateAdded(productDTO.containsKey("dateAdded") ? LocalDateTime.parse(productDTO.get("dateAdded").toString()) : LocalDateTime.now());
        product.setSale(productDTO.containsKey("sale") ? Integer.parseInt(productDTO.get("sale").toString()) : 0);
        product.setCategory(category);
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