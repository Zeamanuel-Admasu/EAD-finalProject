package com.restaurant.table_reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.restaurant.table_reservation.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class TableReservationApplication {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(TableReservationApplication.class, args);
    }

    @PostConstruct
    public void init() {
        userService.preSeedAdmin();
    }
}
