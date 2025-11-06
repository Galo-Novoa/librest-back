package com.galonovoa.librest.controller;

import com.galonovoa.librest.dto.JwtResponse;
import com.galonovoa.librest.dto.LoginRequest;
import com.galonovoa.librest.dto.RegisterRequest;
import com.galonovoa.librest.model.ERole;
import com.galonovoa.librest.model.Role;
import com.galonovoa.librest.model.User;
import com.galonovoa.librest.repository.RoleRepository;
import com.galonovoa.librest.repository.UserRepository;
import com.galonovoa.librest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, 
                         RoleRepository roleRepository, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        // Verificar si el usuario existe
        var user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        // Verificar contraseña
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Error: Contraseña incorrecta.");
        }

        // Verificar si el usuario está habilitado
        if (!user.isEnabled()) {
            throw new RuntimeException("Error: Usuario deshabilitado.");
        }

        // Obtener roles
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        // Crear respuesta (simulando JWT por ahora)
        JwtResponse jwtResponse = new JwtResponse(
                "simulated-jwt-token-" + System.currentTimeMillis(), // Token simulado
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        
        // Verificar si el email ya existe
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso.");
        }

        // Crear nuevo usuario
        User user = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        // Asignar roles
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        roles.add(userRole);

        // Si es el primer usuario, hacerlo admin
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol admin no encontrado."));
            roles.add(adminRole);
        }

        user.setRoles(roles);

        // Guardar usuario
        User savedUser = userService.createUser(user);

        // Obtener roles para la respuesta
        List<String> roleNames = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        // Crear respuesta
        JwtResponse jwtResponse = new JwtResponse(
                "simulated-jwt-token-" + System.currentTimeMillis(), // Token simulado
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                roleNames
        );

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // Por ahora devolvemos un usuario simulado
        // En una implementación real, obtendrías el usuario del contexto de seguridad
        User currentUser = userRepository.findByEmail("admin@librest.com")
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(Map.of(
            "id", currentUser.getId(),
            "email", currentUser.getEmail(),
            "firstName", currentUser.getFirstName(),
            "lastName", currentUser.getLastName(),
            "isAdmin", currentUser.getRoles().stream()
                    .anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)
        ));
    }
}