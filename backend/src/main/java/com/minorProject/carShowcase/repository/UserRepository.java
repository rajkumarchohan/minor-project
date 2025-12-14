package com.minorProject.carShowcase.repository;

import com.minorProject.carShowcase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // For UserDetailsServiceImpl
    User findByUsername(String username);

    // For registration check
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}