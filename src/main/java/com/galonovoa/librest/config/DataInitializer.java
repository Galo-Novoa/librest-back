package com.galonovoa.librest.config;

import com.galonovoa.librest.model.ERole;
import com.galonovoa.librest.model.Role;
import com.galonovoa.librest.model.User;
import com.galonovoa.librest.repository.RoleRepository;
import com.galonovoa.librest.repository.UserRepository;
import com.galonovoa.librest.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryService categoryService, RoleRepository roleRepository, 
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryService = categoryService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
        categoryService.initializeDefaultCategories();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role(ERole.ROLE_USER);
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            Role moderatorRole = new Role(ERole.ROLE_MODERATOR);

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
            roleRepository.save(moderatorRole);
            
            System.out.println("✅ Roles inicializados correctamente");
        }
    }

    private void initializeAdminUser() {
        if (userRepository.findByEmail("admin@librest.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Librest");
            admin.setEmail("admin@librest.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
            admin.setRoles(Set.of(adminRole));
            
            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado: admin@librest.com / admin123");
        }
        
        if (userRepository.findByEmail("usuario@test.com").isEmpty()) {
            User user = new User();
            user.setFirstName("Usuario");
            user.setLastName("Test");
            user.setEmail("usuario@test.com");
            user.setPassword(passwordEncoder.encode("usuario123"));
            
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            user.setRoles(Set.of(userRole));
            
            userRepository.save(user);
            System.out.println("✅ Usuario normal creado: usuario@test.com / usuario123");
        }
    }
}