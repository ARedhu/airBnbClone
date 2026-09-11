package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
