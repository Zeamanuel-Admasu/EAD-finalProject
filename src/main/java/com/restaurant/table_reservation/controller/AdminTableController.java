package com.restaurant.table_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.restaurant.table_reservation.entity.TableEntity;
import com.restaurant.table_reservation.repository.TableRepository;

@Controller
@RequestMapping("/admin/tables")
public class AdminTableController {

    @Autowired
    private TableRepository tableRepository;

    // View all tables
    @GetMapping
    public String viewTables(Model model) {
        model.addAttribute("tables", tableRepository.findAll());
        return "admin-view-tables";
    }

    // Add new table
    @GetMapping("/add")
    public String addTableForm() {
        return "admin-add-table";
    }

    @PostMapping("/add")
    public String addTable(@RequestParam String tableType,
                           @RequestParam int numberOfSeats,
                           @RequestParam int floorNumber) {
        TableEntity table = new TableEntity();
        table.setTableType(tableType);
        table.setNumberOfSeats(numberOfSeats);
        table.setFloorNumber(floorNumber);
        tableRepository.save(table);
        return "redirect:/admin/tables";
    }

    // Edit table
    @GetMapping("/edit/{id}")
    public String editTableForm(@PathVariable Long id, Model model) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table ID"));
        model.addAttribute("table", table);
        return "admin-edit-table";
    }

    @PostMapping("/edit/{id}")
    public String editTable(@PathVariable Long id,
                            @RequestParam String tableType,
                            @RequestParam int numberOfSeats,
                            @RequestParam int floorNumber) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table ID"));
        table.setTableType(tableType);
        table.setNumberOfSeats(numberOfSeats);
        table.setFloorNumber(floorNumber);
        tableRepository.save(table);
        return "redirect:/admin/tables";
    }

    // Delete table
    @PostMapping("/delete/{id}")
    public String deleteTable(@PathVariable Long id) {
        tableRepository.deleteById(id);
        return "redirect:/admin/tables";
    }
}
