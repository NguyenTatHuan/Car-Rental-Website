package com.springprojects.repository;

import com.springprojects.entity.TwoFactorToken;
import com.springprojects.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TwoFactorTokenRepository extends JpaRepository<TwoFactorToken, UUID> {

    Optional<TwoFactorToken> findByUser(User user);

    void deleteByUser(User user);

    boolean existsByUser(User user);

}
