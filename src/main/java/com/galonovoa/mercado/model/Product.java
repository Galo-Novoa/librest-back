package com.galonovoa.mercado.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String image;

    private static final String DEFAULT_PUBLISHER = "admin";
    
    // Campos existentes
    private Double rating = 0.0;
    private String publisher = DEFAULT_PUBLISHER;
    private LocalDateTime dateAdded = LocalDateTime.now();
    private Integer sale = 0;

    // Nuevo campo: Categoría
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {}
    
    public Product(String name, BigDecimal price, String description, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }
    
    public Product(String name, BigDecimal price, String description, String image, ProductOptions options) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        if (options != null) {
            this.rating = options.rating != null ? options.rating : 0.0;
            this.publisher = options.publisher != null ? options.publisher : DEFAULT_PUBLISHER;
            this.dateAdded = options.dateAdded != null ? options.dateAdded : LocalDateTime.now();
            this.sale = options.sale != null ? options.sale : 0;
            this.category = options.category;
        } else {
            this.rating = 0.0;
            this.publisher = DEFAULT_PUBLISHER;
            this.dateAdded = LocalDateTime.now();
            this.sale = 0;
            this.category = null;
        }
    }

    /**
     * Helper class to group optional product parameters and keep constructor parameter count low.
     * Usage: new Product(name, price, desc, image, ProductOptions.builder().rating(4.5).publisher("x").build());
     */
    public static class ProductOptions {
        private Double rating;
        private String publisher;
        private LocalDateTime dateAdded;
        private Integer sale;
        private Category category;

        private ProductOptions() {}

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Double rating;
            private String publisher;
            private LocalDateTime dateAdded;
            private Integer sale;
            private Category category;

            public Builder rating(Double rating) { this.rating = rating; return this; }
            public Builder publisher(String publisher) { this.publisher = publisher; return this; }
            public Builder dateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; return this; }
            public Builder sale(Integer sale) { this.sale = sale; return this; }
            public Builder category(Category category) { this.category = category; return this; }

            public ProductOptions build() {
                ProductOptions opts = new ProductOptions();
                opts.rating = this.rating;
                opts.publisher = this.publisher;
                opts.dateAdded = this.dateAdded;
                opts.sale = this.sale;
                opts.category = this.category;
                return opts;
            }
        }
    }

    // Getters y Setters existentes...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public LocalDateTime getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded = dateAdded; }
    public Integer getSale() { return sale; }
    public void setSale(Integer sale) { this.sale = sale; }
    
    // Nuevo getter y setter para categoría
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}