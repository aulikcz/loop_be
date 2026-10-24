package com.aulikcz.loop.game;

import com.aulikcz.loop.user.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {
    List<MembershipEntity> findByAppUser(AppUserEntity user);
    Optional<MembershipEntity> findByAppUserIdAndGameId(Long userId, Long gameId);
}
