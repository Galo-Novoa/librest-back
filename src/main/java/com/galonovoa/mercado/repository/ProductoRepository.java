package com.galonovoa.mercado.repository;
import com.galonovoa.mercado.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {}