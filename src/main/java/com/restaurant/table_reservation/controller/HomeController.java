package com.restaurant.table_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Ensure `home.html` exists in `resources/templates/`
    }
    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("userName", authentication.getName()); // Pass the username to the view
        }
        return "home"; // Maps to home.html in resources/templates/
    }
}

