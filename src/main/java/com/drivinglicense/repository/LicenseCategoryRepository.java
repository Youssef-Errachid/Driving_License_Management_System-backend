package com.drivinglicense.repository;

import com.drivinglicense.entity.LicenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseCategoryRepository extends JpaRepository<LicenseCategory, Long> {
}