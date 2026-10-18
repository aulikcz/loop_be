package com.aulikcz.loop.gamesystem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameSystemRepository extends JpaRepository<GameSystemEntity, Long> {
    Optional<GameSystemEntity> findByCode(String code);
}
