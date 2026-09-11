package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
