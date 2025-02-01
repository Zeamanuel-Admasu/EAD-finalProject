package com.restaurant.table_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.restaurant.table_reservation.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
//
//    @GetMapping("/login")
//    public String login(@RequestParam(required = false) String role, Model model) {
//        model.addAttribute("role", role != null ? role : "User");
//        return "login"; // Points to login.html
//    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup"; // Points to signup.html
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String fullName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "signup";
        }

        try {
            userService.registerUser(fullName, email, password);

            // Automatically login the user after signup
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "signup";
        }

        return "redirect:/reservation"; // Redirect to user dashboard
    }
}
