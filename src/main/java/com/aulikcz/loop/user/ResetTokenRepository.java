package com.aulikcz.loop.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, Long> {
    Optional<ResetTokenEntity> findByTokenHash(String tokenHash);
}
