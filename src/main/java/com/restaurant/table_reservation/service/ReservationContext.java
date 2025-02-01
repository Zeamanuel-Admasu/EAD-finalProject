package com.restaurant.table_reservation.service;

import com.restaurant.table_reservation.entity.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ReservationContext {

    private Reservation tempReservation;

    public Reservation getTempReservation() {
        return tempReservation;
    }

    public void setTempReservation(Reservation reservation) {
        this.tempReservation = reservation;
    }
}
