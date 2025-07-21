package com.springprojects.repository;

import com.springprojects.entity.CarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarTypeRepository extends JpaRepository<CarType, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

}
