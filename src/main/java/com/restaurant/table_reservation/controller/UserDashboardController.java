package com.restaurant.table_reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.restaurant.table_reservation.config.CustomUserDetails;
import com.restaurant.table_reservation.entity.Reservation;
import com.restaurant.table_reservation.repository.ReservationRepository;
import com.restaurant.table_reservation.repository.TableRepository;

@Controller
@RequestMapping("/user")
public class UserDashboardController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository; // Inject TableRepository

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showUserDashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String fullName = userDetails.getFullName();
            Long userId = userDetails.getId(); // Add ID to CustomUserDetails if not already present.

            // Fetch user reservations
            List<Reservation> userReservations = reservationRepository.findByUserId(userId);
            model.addAttribute("userName", fullName);
            model.addAttribute("reservations", userReservations);
        }
        return "user-dashboard"; // Points to user-dashboard.html
    }

    @PostMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, Authentication authentication) {
        // Ensure that the user is authorized to delete the reservation (check ownership)
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

        // Check if the logged-in user is the owner of the reservation
        Long loggedInUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        if (!reservation.getUser().getId().equals(loggedInUserId)) {
            throw new IllegalStateException("You can only delete your own reservations.");
        }

        // Update the associated table's status to available
        Long tableId = reservation.getTableId();
        if (tableId != null) {
            tableRepository.findById(tableId).ifPresent(table -> {
                table.setReserved(false);
                table.setReservationDateTime(null); // Clear the reservation date
                tableRepository.save(table);
            });
        }

        // Delete the reservation
        reservationRepository.delete(reservation);

        // Redirect back to the dashboard
        return "redirect:/user/dashboard";
    }
}
