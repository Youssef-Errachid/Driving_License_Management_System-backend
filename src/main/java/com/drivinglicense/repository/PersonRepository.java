package com.drivinglicense.repository;

import com.drivinglicense.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByNationalNumber(String nationalNumber);

    boolean existsByNationalNumber(String nationalNumber);

    @Query("SELECT p FROM Person p WHERE " +
            "LOWER(p.nationalNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Person> search(@Param("query") String query);
}