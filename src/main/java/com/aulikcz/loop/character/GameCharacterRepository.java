package com.aulikcz.loop.character;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameCharacterRepository extends JpaRepository<GameCharacterEntity, Long> {
    List<GameCharacterEntity> findByAppUserIdOrderByCreatedAtDesc(Long userId);
    Optional<GameCharacterEntity> findByIdAndAppUserId(Long id, Long userId);
    List<GameCharacterEntity> findByMembershipGameIdOrderByNameAsc(Long gameId);
    List<GameCharacterEntity> findByMembershipGameIdAndAppUserIdOrderByNameAsc(Long gameId, Long userId);
    Optional<GameCharacterEntity> findByIdAndMembershipGameId(Long id, Long gameId);
}
