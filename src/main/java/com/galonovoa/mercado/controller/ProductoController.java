package com.galonovoa.mercado.controller;
import com.galonovoa.mercado.model.Producto;
import com.galonovoa.mercado.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.galonovoa.mercado.repository.ProductoRepository;
import java.util.List;

@RestController
public class ProductoController {
        private final ProductoService service;

        public ProductoController(ProductoService service) {
            this.service = service;
        }

        @GetMapping("/productos")
        public List<Producto> obtenerProductos() {
            return service.obtenerProductos();
        }
}