package com.galonovoa.librest.controller;

import com.galonovoa.librest.model.Product;
import com.galonovoa.librest.model.Category;
import com.galonovoa.librest.service.ProductService;
import com.galonovoa.librest.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Map<String, Object>> getProducts() {
        List<Product> products = service.getProducts();
        
        return products.stream().map(product -> {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id", product.getId());
            productMap.put("name", product.getName());
            productMap.put("price", product.getPrice());
            productMap.put("description", product.getDescription());
            productMap.put("image", product.getImage());
            productMap.put("rating", product.getRating());
            productMap.put("dateAdded", product.getDateAdded());
            productMap.put("sale", product.getSale());
            
            if (product.getCategory() != null) {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("id", product.getCategory().getId());
                categoryMap.put("name", product.getCategory().getName());
                categoryMap.put("description", product.getCategory().getDescription());
                productMap.put("category", categoryMap);
            } else {
                productMap.put("category", null);
            }
            
            productMap.put("publisher", product.getPublisher());
            
            return productMap;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Map<String, Object> productDTO) {
        try {
            System.out.println("📥 Recibiendo producto: " + productDTO);
            
            Object categoryIdObj = productDTO.get("categoryId");
            Category category = null;
            
            if (categoryIdObj != null) {
                try {
                    Long categoryId = Long.parseLong(categoryIdObj.toString());
                    category = categoryService.getCategoryById(categoryId).orElse(null);
                    System.out.println("✅ Categoría encontrada: " + (category != null ? category.getName() : "null"));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ categoryId no es un número válido: " + categoryIdObj);
                }
            }

            Product product = new Product();
            
            if (productDTO.get("name") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre es requerido"));
            }
            product.setName(productDTO.get("name").toString());
            
            if (productDTO.get("price") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El precio es requerido"));
            }
            try {
                product.setPrice(new BigDecimal(productDTO.get("price").toString()));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Precio inválido"));
            }
            
            if (productDTO.get("description") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "La descripción es requerida"));
            }
            product.setDescription(productDTO.get("description").toString());
            
            if (productDTO.get("image") != null) {
                product.setImage(productDTO.get("image").toString());
            } else {
                product.setImage("https://via.placeholder.com/300x300?text=Sin+Imagen");
            }
            
            if (productDTO.containsKey("rating")) {
                try {
                    product.setRating(Double.parseDouble(productDTO.get("rating").toString()));
                } catch (NumberFormatException e) {
                    product.setRating(0.0);
                }
            } else {
                product.setRating(0.0);
            }
            
            product.setDateAdded(LocalDateTime.now());
            
            if (productDTO.containsKey("sale")) {
                try {
                    product.setSale(Integer.parseInt(productDTO.get("sale").toString()));
                } catch (NumberFormatException e) {
                    product.setSale(0);
                }
            } else {
                product.setSale(0);
            }
            
            product.setCategory(category);
            
            if (productDTO.containsKey("publisher")) {
                product.setPublisher(productDTO.get("publisher").toString());
            }
            
            Product saved = service.saveProduct(product);
            System.out.println("✅ Producto guardado exitosamente - ID: " + saved.getId() + ", Nombre: " + saved.getName());
            
            return ResponseEntity.ok(createProductResponse(saved));
            
        } catch (Exception e) {
            System.out.println("❌ Error al crear producto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProductPartial(
        @PathVariable @NonNull Long id,
        @RequestBody Map<String, Object> updates
    ) {
        try {
            Product updated = service.updateProductPartial(id, updates);
            return ResponseEntity.ok(createProductResponse(updated));
        } catch (RuntimeException e) {
            System.out.println("❌ Error al actualizar producto " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@NonNull @PathVariable Long id) {
        try {
            service.deleteProduct(id);
            System.out.println("✅ Producto eliminado - ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar producto " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable @NonNull Long id) {
        try {
            List<Product> products = service.getProducts();
            Product product = products.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (product != null) {
                return ResponseEntity.ok(createProductResponse(product));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener producto " + id + ": " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    private Map<String, Object> createProductResponse(Product product) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", product.getId());
        response.put("name", product.getName());
        response.put("price", product.getPrice());
        response.put("description", product.getDescription());
        response.put("image", product.getImage());
        response.put("rating", product.getRating());
        response.put("dateAdded", product.getDateAdded());
        response.put("sale", product.getSale());
        response.put("publisher", product.getPublisher());
        
        if (product.getCategory() != null) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", product.getCategory().getId());
            categoryMap.put("name", product.getCategory().getName());
            categoryMap.put("description", product.getCategory().getDescription());
            response.put("category", categoryMap);
        } else {
            response.put("category", null);
        }
        
        return response;
    }
}