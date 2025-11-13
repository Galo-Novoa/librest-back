package com.galonovoa.librest.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String image;

    // Campos existentes
    private Double rating = 0.0;

    // Relación con User (propietario del producto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // ✅ EVITA RECURSIÓN
    private User user;

    private LocalDateTime dateAdded = LocalDateTime.now();
    private Integer sale = 0;

    // Relación con Categoría
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"products"}) // ✅ EVITA RECURSIÓN
    private Category category;

    // Campo transitorio para compatibilidad con frontend
    @Transient
    private String publisher;

    public Product() {
    }

    public Product(String name, BigDecimal price, String description, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    // ✅ GETTER para publisher que resuelve la recursión
    public String getPublisher() {
        if (this.user != null) {
            return this.user.getEmail();
        }
        return this.publisher != null ? this.publisher : "admin";
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // Resto de getters y setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}