package com.galonovoa.librest.repository;

import com.galonovoa.librest.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}