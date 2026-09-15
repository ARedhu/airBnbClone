package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.dto.HotelPriceDto;
import com.Ashish.airBnbClone.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {

    // We are saying that a single hotel can have different min price on each day which comes from the cheapest room price on that date for that hotel.
    // But we have to show the list of hotels with their price b/w these date ranges.
    // So, we are giving frontend the average of minimum of hotel price b/w these dates.
    @Query("""
            SELECT new com.Ashish.airBnbClone.dto.HotelPriceDto(i.hotel, AVG(i.price))
            FROM HotelMinPrice i
            WHERE i.hotel.city = :city
                AND i.date BETWEEN :startDate AND :endDate
                AND i.hotel.active = true
            GROUP BY i.hotel
            """)
    Page<HotelPriceDto> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );
}
