package com.galonovoa.librest.controller;

import com.galonovoa.librest.model.Product;
import com.galonovoa.librest.model.Category;
import com.galonovoa.librest.service.ProductService;
import com.galonovoa.librest.service.CategoryService;
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
        
        // USAR FECHA ACTUAL DEL SERVIDOR
        product.setDateAdded(LocalDateTime.now());
        
        product.setSale(productDTO.containsKey("sale") ? Integer.parseInt(productDTO.get("sale").toString()) : 0);
        product.setCategory(category);
        
        // ✅ USAR service.saveProduct() en lugar de repository
        Product saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProductPartial(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates
    ) {
        try {
            // ✅ USAR service.updateProductPartial() - este método ya maneja todo
            Product updated = service.updateProductPartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            // ✅ USAR service.deleteProduct()
            service.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}