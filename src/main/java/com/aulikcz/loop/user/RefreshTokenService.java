package com.aulikcz.loop.user;

import com.aulikcz.loop.common.BadLogin;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    public static final Duration REFRESH_TTL = Duration.ofDays(14);

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String create(AppUserEntity appUser){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String hash = hash(raw);
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setAppUser(appUser);
        refreshTokenEntity.setExpiresAt(OffsetDateTime.now().plus(REFRESH_TTL));
        refreshTokenEntity.setTokenHash(hash);
        refreshTokenRepository.save(refreshTokenEntity);
        return raw;
    }

    public RefreshTokenEntity findValidToken(String raw){
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenHash(hash(raw)).orElseThrow(() -> new BadLogin("Invalid token"));
        if(refreshTokenEntity.getRevokedAt() != null || refreshTokenEntity.getExpiresAt().isBefore(OffsetDateTime.now())){
            throw new BadLogin("Invalid token");
        }
        return refreshTokenEntity;
    }

    private String hash(String raw) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
