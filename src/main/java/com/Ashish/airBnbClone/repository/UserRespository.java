package com.Ashish.airBnbClone.repository;

import com.Ashish.airBnbClone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
