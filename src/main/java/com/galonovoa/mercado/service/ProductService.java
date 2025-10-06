package com.galonovoa.mercado.service;

import com.galonovoa.mercado.model.Product;
import com.galonovoa.mercado.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product saveProduct(Product p) {
        return repository.save(p);
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public Product updateProductPartial(Long id, Map<String, Object> updates) {
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