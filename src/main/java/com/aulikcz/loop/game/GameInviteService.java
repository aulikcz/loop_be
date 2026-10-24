package com.aulikcz.loop.game;

import com.aulikcz.loop.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.OffsetDateTime;

@Service
public class GameInviteService {

    static final Duration INVITE_TTL = Duration.ofDays(7);
    private static final String ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GameInviteRepository gameInviteRepository;
    private final GameRepository gameRepository;
    private final MembershipRepository membershipRepository;
    private final AppUserRepository appUserRepository;

    public GameInviteService(GameInviteRepository gameInviteRepository, GameRepository gameRepository, MembershipRepository membershipRepository, AppUserRepository appUserRepository){
        this.gameInviteRepository = gameInviteRepository;
        this.gameRepository = gameRepository;
        this.membershipRepository = membershipRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public InviteResponse regenerate(Long userId, Long gameId) {
        requireNarrator(userId, gameId);

        gameInviteRepository.findByGameIdAndRevokedAtIsNull(gameId).ifPresent(old -> {
            old.setRevokedAt(OffsetDateTime.now());
            gameInviteRepository.saveAndFlush(old);
        });

        GameInviteEntity newGameInvite = new GameInviteEntity();
        newGameInvite.setGame(gameRepository.getReferenceById(gameId));
        newGameInvite.setCreatedBy(appUserRepository.getReferenceById(userId));
        String inviteCode = generateCode();
        newGameInvite.setCode(inviteCode);
        OffsetDateTime expiresAt = OffsetDateTime.now().plus(INVITE_TTL);
        newGameInvite.setExpiresAt(expiresAt);
        gameInviteRepository.save(newGameInvite);
        return new InviteResponse(inviteCode, expiresAt);
    }

    @Transactional(readOnly = true)
    public InviteResponse getCurrent(Long userId, Long gameId) {
        requireNarrator(userId, gameId);
        GameInviteEntity gameInviteEntity = gameInviteRepository.findByGameIdAndRevokedAtIsNull(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite code not found"));
        return new InviteResponse(gameInviteEntity.getCode(), gameInviteEntity.getExpiresAt());
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private void requireNarrator(Long userId, Long gameId){
        MembershipEntity membership = membershipRepository.findByAppUserIdAndGameId(userId, gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        if(membership.getPlayerRole().getRole() != PlayerRoleType.NARRATOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a NARRATOR");
        }
    }
}
