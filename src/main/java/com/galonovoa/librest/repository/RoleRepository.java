// ./src/main/java/com/galonovoa/librest/repository/RoleRepository.java
package com.galonovoa.librest.repository;

import com.galonovoa.librest.model.Role;
import com.galonovoa.librest.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}