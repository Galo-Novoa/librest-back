package com.galonovoa.mercado.repository;
import com.galonovoa.mercado.model.Producto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository repository;

    public DataLoader(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Producto("Laptop", 1500, "Laptop potente", "/laptop.jpeg"));
        repository.save(new Producto("Mouse", 20, "Mouse ergonómico", "/mouse.png"));
        repository.save(new Producto("Teclado", 45, "Teclado mecánico", "/teclado.png"));
    }
}