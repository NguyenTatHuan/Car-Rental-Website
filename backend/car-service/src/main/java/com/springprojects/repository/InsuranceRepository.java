package com.springprojects.repository;

import com.springprojects.entity.Insurance;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InsuranceRepository extends JpaRepository<Insurance, UUID> {

    List<Insurance> findAllByCar_Id(UUID carId);

}
