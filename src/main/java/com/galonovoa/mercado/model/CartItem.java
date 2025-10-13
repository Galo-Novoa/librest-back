package com.galonovoa.mercado.model;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;

    private String username;

    public CartItem() {}

    public CartItem(Product product, int quantity, String username) {
        this.product = product;
        this.quantity = quantity;
        this.username = username;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}