package com.galonovoa.mercado.service;

import com.galonovoa.mercado.model.Product;
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
}