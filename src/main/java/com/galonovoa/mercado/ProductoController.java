package com.galonovoa.mercado;
import com.galonovoa.mercado.model.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/productos")
    public List<Producto> obtenerProductos() {
        return repository.findAll();
    }
}