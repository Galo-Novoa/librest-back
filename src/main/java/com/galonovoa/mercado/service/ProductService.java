package com.galonovoa.mercado.service;

import com.galonovoa.mercado.model.Product;
import com.galonovoa.mercado.dto.ProductDTO;
import com.galonovoa.mercado.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public Product updateProduct(Long id, ProductDTO updates) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (updates.getName() != null) {
            product.setName(updates.getName());
        }
        if (updates.getPrice() != null) {
            product.setPrice(updates.getPrice());
        }
        if (updates.getDescription() != null) {
            product.setDescription(updates.getDescription());
        }
        if (updates.getImage() != null) {
            product.setImage(updates.getImage());
        }

        return repository.save(product);
    }
}