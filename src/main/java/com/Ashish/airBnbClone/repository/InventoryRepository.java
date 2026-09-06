package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.entity.Inventory;
import com.Ashish.airBnbClone.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByDateAfterAndRoom(LocalDate today, Room room);
}
