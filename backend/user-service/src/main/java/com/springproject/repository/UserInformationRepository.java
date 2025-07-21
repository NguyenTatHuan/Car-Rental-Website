package com.springproject.repository;

import com.springproject.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {

    Optional<UserInformation> findFirstByEmail(String email);

    Optional<UserInformation> findFirstByCitizenID(String citizenID);

    Optional<UserInformation> findFirstByPhone(String phone);

    Optional<UserInformation> findByUserId(UUID userId);

}
