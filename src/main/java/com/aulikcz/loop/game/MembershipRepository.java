package com.aulikcz.loop.game;

import com.aulikcz.loop.user.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {
    List<MembershipEntity> findByAppUser(AppUserEntity user);
}
