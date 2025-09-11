package com.galonovoa.mercado.repository;
import com.galonovoa.mercado.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}