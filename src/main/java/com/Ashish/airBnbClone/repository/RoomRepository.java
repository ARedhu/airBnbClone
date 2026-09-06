package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}

