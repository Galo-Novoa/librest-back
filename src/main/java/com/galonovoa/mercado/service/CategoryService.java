package com.galonovoa.mercado.service;

import com.galonovoa.mercado.model.Category;
import com.galonovoa.mercado.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAllCategories() {
        return repository.findAllByOrderByNameAsc();
    }

    public Optional<Category> getCategoryById(Long id) {
        return repository.findById(id);
    }

    public Category createCategory(Category category) {
        return repository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        
        return repository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        repository.delete(category);
    }

    // Crear categorías por defecto
    public void initializeDefaultCategories() {
        if (repository.count() == 0) {
            List<Category> defaultCategories = List.of(
                new Category("Electrónicos", "Teléfonos, computadoras, tablets y más"),
                new Category("Hogar", "Muebles, decoración y artículos para el hogar"),
                new Category("Deportes", "Equipamiento deportivo y ropa de ejercicio"),
                new Category("Moda", "Ropa, calzado y accesorios"),
                new Category("Libros", "Libros, novelas y material educativo"),
                new Category("Juguetes", "Juguetes para niños y juegos"),
                new Category("Salud", "Productos de salud y belleza"),
                new Category("Automotriz", "Repuestos y accesorios para vehículos"),
                new Category("Mascotas", "Alimentos y accesorios para mascotas"),
                new Category("Herramientas", "Herramientas y equipamiento")
            );
            
            repository.saveAll(defaultCategories);
        }
    }
}