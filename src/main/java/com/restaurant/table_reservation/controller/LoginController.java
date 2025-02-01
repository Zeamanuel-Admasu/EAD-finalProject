package com.restaurant.table_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login/user")
    public String userLoginPage(Model model) {
        model.addAttribute("role", "User");
        return "login-user"; // Maps to login.html
    }

    @GetMapping("/login/admin")
    public String adminLoginPage(Model model) {
        model.addAttribute("role", "Admin");
        return "login-admin"; // Maps to login.html
    }
}



