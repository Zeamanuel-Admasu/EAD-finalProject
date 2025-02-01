package com.restaurant.table_reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restaurant.table_reservation.entity.TableEntity;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
	List<TableEntity> findByTableTypeAndFloorNumberAndNumberOfSeats(String tableType, int floorNumber, int numberOfSeats);
	
    List<TableEntity> findByNumberOfSeatsAndTableTypeAndFloorNumberAndIsReservedFalse(
        int numberOfSeats, String tableType, int floorNumber);
    
}
