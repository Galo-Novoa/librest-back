package com.galonovoa.mercado.controller;
import com.galonovoa.mercado.model.Producto;
import com.galonovoa.mercado.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.galonovoa.mercado.repository.ProductoRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        return service.obtenerProductos();
    }

    @PostMapping
    public Producto agregarProducto(@RequestBody Producto nuevoProducto) {
        return service.guardarProducto(nuevoProducto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        service.eliminarProducto(id);
    }
}
