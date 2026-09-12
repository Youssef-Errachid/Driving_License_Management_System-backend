package com.drivinglicense.repository;

import com.drivinglicense.entity.User;
import com.drivinglicense.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    long countByUserStatus(UserStatus userStatus);
}