	package com.restaurant.table_reservation.controller;
	
	import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.restaurant.table_reservation.config.CustomUserDetails;
import com.restaurant.table_reservation.entity.Reservation;
import com.restaurant.table_reservation.entity.TableEntity;
import com.restaurant.table_reservation.entity.users;
import com.restaurant.table_reservation.repository.ReservationRepository;
import com.restaurant.table_reservation.repository.TableRepository;
import com.restaurant.table_reservation.repository.UserRepository;
	
	@Controller
	public class ReservationController {
	
	    @Autowired
	    private ReservationRepository reservationRepository;
	
	    @Autowired
	    private TableRepository tableRepository;
	
	    @Autowired
	    private UserRepository userRepository;
	
	    @GetMapping("/reservation")
	    public String showReservationPage(Authentication authentication, Model model) {
	    	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
	        if (authentication == null || !authentication.isAuthenticated()) {
	            return "redirect:/login";
	        }
	
	        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	        String fullName = userDetails.getFullName();
	        model.addAttribute("userName", fullName);
	
	        return "reservation";
	    }
	
	    @GetMapping("/admin/reservations")
	    public String viewAllReservations(Model model) {
	        List<Reservation> reservations = reservationRepository.findAll();
	        reservations.forEach(res -> System.out.println("Reservation ID: " + res.getId() + ", Payment Status: " + res.isPaymentStatus()));
	        model.addAttribute("reservations", reservations);
	        return "admin-view-reservations";
	    }

	    @PostMapping("/update-payment")
	    public ResponseEntity<String> updatePaymentStatus(@RequestParam Long reservationId) {
	        System.out.println("🔍 Attempting to update payment status for Reservation ID: " + reservationId);
	        
	        int updatedRows = reservationRepository.updatePaymentStatus(reservationId);

	        if (updatedRows > 0) {
	            System.out.println("✅ Payment status updated successfully for Reservation ID: " + reservationId);
	            return ResponseEntity.ok("Payment updated successfully!");
	        } else {
	            System.err.println("❌ ERROR: Payment update failed! No rows affected.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment update failed!");
	        }
	    }
	
	    @PostMapping("/reservation")
	    public String processReservation(
	        @RequestParam String type,
	        @RequestParam int floor,
	        @RequestParam int seats,
	        @RequestParam String reservationDateTime,
	        Authentication authentication,
	        Model model
	    ) {
	        if (authentication == null || !authentication.isAuthenticated()) {
	            return "redirect:/login";
	        }
	
	        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	        Long userId = userDetails.getId();
	        System.out.println("DEBUG: Reservation Type: " + type);
	        System.out.println("DEBUG: Floor: " + floor);
	        System.out.println("DEBUG: Seats: " + seats);
	        System.out.println("DEBUG: Reservation DateTime: " + reservationDateTime);
	
	        users user = userRepository.findById(userId)
	                .orElseThrow(() -> new IllegalStateException("User not found"));
	
	        List<TableEntity> availableTables = tableRepository.findByTableTypeAndFloorNumberAndNumberOfSeats(type, floor, seats);
	
	        if (availableTables.isEmpty()) {
	            model.addAttribute("error", "No tables are available for your criteria.");
	            return "errorPage";
	        }
	
	        TableEntity table = availableTables.get(0);
	        if (table.isReserved()) {
	            model.addAttribute("error", "Selected table is already reserved.");
	            return "alreadyreservedPage";
	        }
	
	        table.setReserved(true);
	        table.setReservationDateTime(LocalDateTime.parse(reservationDateTime));
	        tableRepository.save(table);
	
	        Reservation reservation = new Reservation();
	        reservation.setTableId(table.getId());
	        reservation.setReservationType(type);
	        reservation.setFloorNumber(floor);
	        reservation.setNumberOfSeats(seats);
	        reservation.setReservationDateTime(LocalDateTime.parse(reservationDateTime));
	        reservation.setUser(user);
	        reservation.setPaymentStatus(false);
	        reservationRepository.save(reservation);
	        
	        // Log the reservation ID for debugging
	        System.out.println("DEBUG: Reservation ID is " + reservation.getId());
	
	        // Redirect to the payment page
	        return "redirect:/payment?reservationId=" + reservation.getId();
	    }
	    
	    
	}
	
