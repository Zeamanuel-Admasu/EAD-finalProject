package com.restaurant.table_reservation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import com.restaurant.table_reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query("SELECT r FROM Reservation r WHERE r.stripePaymentIntentId = :paymentIntentId")
    Reservation findByStripePaymentIntentId(@Param("paymentIntentId") String paymentIntentId);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.paymentStatus = true WHERE r.id = :reservationId")
    int updatePaymentStatus(@Param("reservationId") Long reservationId);
}
