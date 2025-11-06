package com.galonovoa.librest.service;

import com.galonovoa.librest.model.Product;
import com.galonovoa.librest.repository.ProductRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.galonovoa.librest.model.User;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final AuthService authService;

    public ProductService(ProductRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product saveProduct(@NonNull Product p) {
        Optional<User> currentUser = authService.getCurrentUser();
        if (currentUser.isPresent()) {
            p.setUser(currentUser.get());
        }
        return repository.save(p);
    }

    public void deleteProduct(@NonNull Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("null")
    public Product updateProductPartial(@NonNull Long id, Map<String, Object> updates) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (updates.get("name") != null) {
            product.setName(updates.get("name").toString());
        }
        if (updates.get("price") != null) {
            product.setPrice(new BigDecimal(updates.get("price").toString()));
        }
        if (updates.get("description") != null) {
            product.setDescription(updates.get("description").toString());
        }
        if (updates.get("image") != null) {
            product.setImage(updates.get("image").toString());
        }

        return repository.save(product);
    }
}