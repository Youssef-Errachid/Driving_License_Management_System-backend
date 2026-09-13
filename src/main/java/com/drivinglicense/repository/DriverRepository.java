package com.drivinglicense.repository;

import com.drivinglicense.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByPerson_NationalNumber(String nationalNumber);

}