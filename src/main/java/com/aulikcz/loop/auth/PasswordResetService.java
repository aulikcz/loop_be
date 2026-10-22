package com.aulikcz.loop.auth;

import com.aulikcz.loop.common.InvalidResetTokenException;
import com.aulikcz.loop.mail.MailSender;
import com.aulikcz.loop.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.aulikcz.loop.common.AuthUtils.normalizeEmail;
import static com.aulikcz.loop.common.AuthUtils.sha256Hex;
import static com.aulikcz.loop.user.AuthProviders.LOCAL;

@Service
public class PasswordResetService {

    static final Duration RESET_TTL = Duration.ofMinutes(30);

    private final AppUserRepository appUserRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final MailSender mailSender;
    private final String frontendUrl;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public PasswordResetService(@Value("${frontend-url}") String frontendUrl, AppUserRepository appUserRepository, ResetTokenRepository resetTokenRepository, MailSender mailSender, LocalCredentialRepository localCredentialRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository){
        this.appUserRepository = appUserRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
        this.localCredentialRepository = localCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void requestReset(String email){
        String normalizedEmail = normalizeEmail(email);
        Optional<AppUserEntity> found = appUserRepository.findByAuthProviderAndProviderUserId(LOCAL, normalizedEmail);
        if(found.isEmpty()){
            return;
        }

        AppUserEntity user = found.get();

        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        ResetTokenEntity resetTokenEntity = new ResetTokenEntity();
        resetTokenEntity.setAppUser(user);
        resetTokenEntity.setExpiresAt(OffsetDateTime.now().plus(RESET_TTL));
        resetTokenEntity.setTokenHash(sha256Hex(token));
        resetTokenRepository.save(resetTokenEntity);

        String url = frontendUrl + "/reset-password?token=" + token;
        mailSender.sendPasswordReset(normalizedEmail, url);
    }



    @Transactional
    public void resetPassword(ResetPasswordRequest request){
        if(request.token().isEmpty()){
            throw new InvalidResetTokenException("Invalid or expired token");
        }
        String hashToken = sha256Hex(request.token());
        ResetTokenEntity resetToken = resetTokenRepository.findByTokenHash(hashToken).orElseThrow(() -> new InvalidResetTokenException("Invalid or expired token"));
        if(resetToken.getUsedAt() != null || resetToken.getExpiresAt().isBefore(OffsetDateTime.now())){
            throw new InvalidResetTokenException("Invalid or expired token");
        }

        LocalCredentialEntity localCredential = localCredentialRepository.findById(resetToken.getAppUser().getId()).orElseThrow(() -> new InvalidResetTokenException("Invalid or expired token"));
        localCredential.setPasswordHash(passwordEncoder.encode(request.newPassword()));

        resetToken.setUsedAt(OffsetDateTime.now());

        List<RefreshTokenEntity> tokens = refreshTokenRepository.findByAppUserAndRevokedAtIsNull(resetToken.getAppUser());
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        for (RefreshTokenEntity t : tokens) {
            t.setRevokedAt(offsetDateTime);
        }
    }
}
