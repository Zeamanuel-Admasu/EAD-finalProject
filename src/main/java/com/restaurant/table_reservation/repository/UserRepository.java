package com.restaurant.table_reservation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.table_reservation.entity.users;

public interface UserRepository extends JpaRepository<users, Long> {
    Optional<users> findByEmail(String email);

    boolean existsByEmail(String email); // Add this line
}
