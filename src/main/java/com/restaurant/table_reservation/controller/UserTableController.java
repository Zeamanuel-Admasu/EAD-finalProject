package com.restaurant.table_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.restaurant.table_reservation.repository.TableRepository;

import java.util.List;
import com.restaurant.table_reservation.entity.TableEntity;

@Controller
public class UserTableController {

    @Autowired
    private TableRepository tableRepository;

    /**
     * Display all available tables to the user.
     *
     * @param model Thymeleaf model to pass data to the view.
     * @return the name of the Thymeleaf template to display the tables.
     */
    @GetMapping("/user/tables")
    public String showAvailableTables(Model model) {
        // Fetch all tables (you can filter or add logic here if needed)
        List<TableEntity> tables = tableRepository.findAll();
        model.addAttribute("tables", tables);
        return "user-tables"; // Name of the Thymeleaf template
    }
}
