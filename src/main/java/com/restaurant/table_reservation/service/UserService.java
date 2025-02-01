package com.restaurant.table_reservation.service;

import com.restaurant.table_reservation.entity.users;
import com.restaurant.table_reservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String fullName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        users newUser = new users();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole("ROLE_USER");
        userRepository.save(newUser);
    }

    public void preSeedAdmin() {
        if (!userRepository.existsByEmail("admin@restaurant.com")) {
            users admin = new users();
            admin.setFullName("Admin");
            admin.setEmail("admin@restaurant.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }
    }
}
