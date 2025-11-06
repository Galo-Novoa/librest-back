package com.galonovoa.librest.service;

import com.galonovoa.librest.model.ERole;
import com.galonovoa.librest.model.User;
import com.galonovoa.librest.model.Product;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    // Obtener el usuario actualmente autenticado
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            String email = authentication.getName();
            return userService.findByEmail(email);
        }
        return Optional.empty();
    }

    // Verificar si el usuario actual es ADMIN
    public boolean isAdmin() {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.map(user -> user.hasRole(ERole.ROLE_ADMIN))
                         .orElse(false);
    }

    // Verificar si el usuario actual es el propietario del producto
    public boolean isProductOwner(Product product) {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.map(user -> {
            if (product.getUser() != null) {
                return product.getUser().getEmail().equals(user.getEmail());
            }
            return false;
        }).orElse(false);
    }

    // Verificar si puede editar/eliminar un producto
    public boolean canEditProduct(Product product) {
        return isAdmin() || isProductOwner(product);
    }

    // Obtener el email del usuario actual
    public String getCurrentUserEmail() {
        return getCurrentUser().map(User::getEmail).orElse(null);
    }
}