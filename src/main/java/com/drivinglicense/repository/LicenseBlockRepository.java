package com.drivinglicense.repository;

import com.drivinglicense.entity.LicenseBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseBlockRepository extends JpaRepository<LicenseBlock, Long> {

    Optional<LicenseBlock> findByLicense_IdAndUnblockingDateIsNull(Long licenseId);

    List<LicenseBlock> findByLicense_Id(Long licenseId);
}