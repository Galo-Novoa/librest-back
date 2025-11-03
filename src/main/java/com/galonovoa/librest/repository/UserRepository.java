// ./src/main/java/com/galonovoa/librest/repository/UserRepository.java
package com.galonovoa.librest.repository;

import com.galonovoa.librest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}