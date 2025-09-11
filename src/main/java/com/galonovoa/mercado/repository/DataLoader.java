package com.galonovoa.mercado.repository;
import com.galonovoa.mercado.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository repository;

    public DataLoader(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Product("Laptop", 1500, "Laptop potente", "/laptop.jpeg"));
        repository.save(new Product("Mouse", 20, "Mouse ergonómico", "/mouse.png"));
        repository.save(new Product("Teclado", 45, "Teclado mecánico", "/teclado.png"));
    }
}