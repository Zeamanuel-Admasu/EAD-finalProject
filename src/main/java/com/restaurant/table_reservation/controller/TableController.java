package com.restaurant.table_reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.restaurant.table_reservation.config.CustomUserDetails;
import com.restaurant.table_reservation.entity.TableEntity;
import com.restaurant.table_reservation.repository.TableRepository;

@Controller
public class TableController {

    @Autowired
    private TableRepository tableRepository;

    @GetMapping("/available-tables")
    public String showAvailableTables(Authentication authentication, Model model) {
        List<TableEntity> availableTables = tableRepository.findAll();
        model.addAttribute("availableTables", availableTables);
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = ((CustomUserDetails) authentication.getPrincipal()).getFullName();
            model.addAttribute("userName", userName);
        }
        return "available-tables"; // This refers to the Thymeleaf template
    }
}
