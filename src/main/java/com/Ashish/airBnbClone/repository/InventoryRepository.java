package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.entity.Hotel;
import com.Ashish.airBnbClone.entity.Inventory;
import com.Ashish.airBnbClone.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByRoom(Room room);

    @Query("""
        SELECT DISTINCT i.hotel from Inventory i
                WHERE i.city = :city
                    AND i.date BETWEEN :startDate AND :endDate
                    AND i.closed = false
                    AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
                GROUP BY i.hotel, i.room
                HAVING COUNT(i.date) = :daysCount
        """)
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("daysCount") Long daysCount,
            Pageable pageable
    );


    @Query("""
        SELECT i 
        FROM Inventory i
        WHERE i.room.id = :roomId
            AND i.date BETWEEN :startDate AND :endDate
            AND i.closed = false
            AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE) // Pessimistic locking prevents two concurrent transactions from changing the same inventory row simultaneously. This lock will be released automatically when the trasaction is complete which is present inside of BookingService.
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
    );
}
