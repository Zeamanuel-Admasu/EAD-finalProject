package com.restaurant.table_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurant.table_reservation.entity.Reservation;
import com.restaurant.table_reservation.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation saveReservation(Reservation reservation) {
        // Add logic to check availability if needed
        return reservationRepository.save(reservation);
    }
    public void updatePaymentStatus(Long reservationId, boolean paymentStatus) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setPaymentStatus(paymentStatus);
        reservationRepository.save(reservation);
    }
}
