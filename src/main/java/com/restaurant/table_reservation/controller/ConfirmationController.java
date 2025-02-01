package com.restaurant.table_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.restaurant.table_reservation.entity.Reservation;
import com.restaurant.table_reservation.repository.ReservationRepository;
import com.restaurant.table_reservation.service.ReservationContext;

@Controller
public class ConfirmationController {
	private final ReservationContext reservationContext;

	@Autowired
	public ConfirmationController(ReservationContext reservationContext) {
		this.reservationContext = reservationContext;
	}
	@Autowired
    private ReservationRepository reservationRepository;
	@GetMapping("/confirmation")
	public String showConfirmation(@RequestParam Long reservationId, Model model) {
	    Reservation reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));
	    
	    model.addAttribute("reservation", reservation);

	    return "confirmation";
	}


}
