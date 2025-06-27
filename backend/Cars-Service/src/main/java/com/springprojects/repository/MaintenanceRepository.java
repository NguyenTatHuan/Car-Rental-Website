package com.springprojects.repository;

import com.springprojects.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MaintenanceRepository extends JpaRepository<Maintenance, UUID> {
    List<Maintenance> findAllByCar_Id(UUID carId);
}
