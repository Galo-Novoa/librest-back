package com.galonovoa.mercado.service;

import com.galonovoa.mercado.model.Producto;
import com.galonovoa.mercado.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> obtenerProductos() {
        return repository.findAll();
    }

    public Producto guardarProducto(Producto p) {
        return repository.save(p);
    }

    public void eliminarProducto(Long id) {
        repository.deleteById(id);
    }
}