package com.springprojects.repository;

import com.springprojects.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {

    Optional<UserInformation> findFirstByEmail(String email);

    Optional<UserInformation> findFirstByCitizenID(String citizenID);

    Optional<UserInformation> findFirstByPhone(String phone);

    Optional<UserInformation> findByUserId(UUID userId);

}
