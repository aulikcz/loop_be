package com.aulikcz.loop.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameInviteRepository extends JpaRepository<GameInviteEntity, Long> {
    Optional<GameInviteEntity> findByGameIdAndRevokedAtIsNull(Long gameId);
    Optional<GameInviteEntity> findByCodeAndRevokedAtIsNull(String code);
}
