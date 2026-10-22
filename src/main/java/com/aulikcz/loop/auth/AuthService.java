package com.aulikcz.loop.auth;

import com.aulikcz.loop.common.BadLogin;
import com.aulikcz.loop.common.ConflictException;
import com.aulikcz.loop.common.InvalidResetTokenException;
import com.aulikcz.loop.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aulikcz.loop.common.AuthUtils.normalizeEmail;
import static com.aulikcz.loop.common.AuthUtils.sha256Hex;
import static com.aulikcz.loop.user.AuthProviders.LOCAL;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AppUserRepository appUserRepository, LocalCredentialRepository localCredentialRepository, PasswordEncoder passwordEncoder, TokenService tokenService, RefreshTokenService refreshTokenService){
        this.appUserRepository = appUserRepository;
        this.localCredentialRepository = localCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public UserDto register(RegisterRequest req) {
        String normalizedEmail = normalizeEmail(req.email());
        boolean userExists = appUserRepository.existsByAuthProviderAndProviderUserId(LOCAL, normalizedEmail);
        if(userExists){
            throw new ConflictException("User already exists");
        }
        AppUserEntity appUser = new AppUserEntity();
        appUser.setAuthProvider(LOCAL);
        appUser.setDisplayName(req.displayName().trim());
        appUser.setEmail(normalizedEmail);
        appUser.setProviderUserId(normalizedEmail);

        appUserRepository.save(appUser);

        LocalCredentialEntity localCredential = new LocalCredentialEntity();
        localCredential.setUser(appUser);
        localCredential.setPasswordHash(passwordEncoder.encode(req.password()));
        localCredentialRepository.save(localCredential);
        return UserDto.from(appUser);
    }

    @Transactional(readOnly = true)
    public String refresh(String raw){
        if (raw == null) {
            throw new BadLogin("Invalid token");
        }
        RefreshTokenEntity validToken = refreshTokenService.findValidToken(raw);
        return tokenService.creatAccessToken(validToken.getAppUser());
    }

    public void logout(String raw){
        if(raw != null) {
            refreshTokenService.revoke(raw);
        }
    }

    public AuthTokens login(LoginRequest login){
        String normalizedEmail = normalizeEmail(login.email());
        AppUserEntity user = appUserRepository.findByAuthProviderAndProviderUserId(LOCAL, normalizedEmail).orElseThrow(() -> new BadLogin("Invalid credentials”"));
        LocalCredentialEntity credentials = localCredentialRepository.findById(user.getId()).orElseThrow(() -> new BadLogin("Invalid credentials”"));
        if(!passwordEncoder.matches(login.password(), credentials.getPasswordHash())){
            throw new BadLogin("Invalid credentials”");
        }
        String accessToken = tokenService.creatAccessToken(user);
        String refreshToken = refreshTokenService.create(user);
        return new AuthTokens(accessToken, refreshToken);
    }
}
