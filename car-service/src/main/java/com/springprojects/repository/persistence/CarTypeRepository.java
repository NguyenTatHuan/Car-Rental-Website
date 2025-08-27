package com.springprojects.repository.persistence;

import com.springprojects.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarTypeRepository extends JpaRepository<CarType, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

}
