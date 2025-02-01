package com.restaurant.table_reservation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.restaurant.table_reservation.entity.Reservation;
import com.restaurant.table_reservation.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.publishable.key}")
    private String stripePublishableKey;

    private final ReservationRepository reservationRepository;

    public PaymentController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public String showPaymentPage(@RequestParam Long reservationId, Model model) {
        // Retrieve the reservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

        // Simulate PaymentIntent creation
        String simulatedClientSecret = "simulated_client_secret_" + reservationId;
        reservation.setStripePaymentIntentId(simulatedClientSecret);
        reservationRepository.save(reservation);

        // Add attributes for the simulated payment page
        model.addAttribute("reservation", reservation);
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("clientSecret", simulatedClientSecret);
        model.addAttribute("stripePublishableKey", "simulated_publishable_key");

        return "payment"; // Points to your payment.html template
    }



    

    @PostMapping
    @Transactional
    public String processPayment(@RequestParam Long reservationId) {
      System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Logger logger = LoggerFactory.getLogger(PaymentController.class);

        try {
          logger.debug("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

            // Log the current payment status
            logger.debug("Current Payment Status: {}", reservation.isPaymentStatus());

            // Update payment status
            reservation.setPaymentStatus(true);
            reservationRepository.save(reservation);

            // Log the updated payment status
            logger.debug("Updated Payment Status: {}", reservation.isPaymentStatus());

            return "redirect:/confirmation?reservationId=" + reservationId;
        } catch (Exception e) {
            logger.error("Error updating payment status: {}", e.getMessage());
            return "errorPage";
        }
    }




}

