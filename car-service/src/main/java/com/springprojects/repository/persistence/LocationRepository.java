package com.springprojects.repository.persistence;

import com.springprojects.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    boolean existsByName(String name);

}
