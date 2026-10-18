package com.aulikcz.loop.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findByAuthProviderAndProviderUserId(String authProvider, String providerUserId);
}
