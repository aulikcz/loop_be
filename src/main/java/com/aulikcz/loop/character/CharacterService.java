package com.aulikcz.loop.character;

import com.aulikcz.loop.game.*;
import com.aulikcz.loop.gamesystem.GameSystemEntity;
import com.aulikcz.loop.gamesystem.GameSystemRepository;
import com.aulikcz.loop.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class CharacterService {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameSystemRepository gameSystemRepository;
    private final AppUserRepository appUserRepository;
    private final GameInviteRepository gameInviteRepository;
    private final MembershipRepository membershipRepository;
    private final PlayerRoleRepository playerRoleRepository;

    public CharacterService(GameCharacterRepository gameCharacterRepository, GameSystemRepository gameSystemRepository, AppUserRepository appUserRepository, GameInviteRepository gameInviteRepository, MembershipRepository membershipRepository, PlayerRoleRepository playerRoleRepository){
        this.gameCharacterRepository = gameCharacterRepository;
        this.gameSystemRepository = gameSystemRepository;
        this.appUserRepository = appUserRepository;
        this.gameInviteRepository = gameInviteRepository;
        this.membershipRepository = membershipRepository;
        this.playerRoleRepository = playerRoleRepository;
    }

    @Transactional
    public CharacterResponse create(Long userId, CreateCharacterRequest req) {
        GameSystemEntity gameSystem = gameSystemRepository.findByCode(req.systemCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neznámý herní systém"));

        GameCharacterEntity character = new GameCharacterEntity();
        character.setAppUser(appUserRepository.getReferenceById(userId));
        character.setGameSystem(gameSystem);
        character.setName(req.name());
        character.setSheet(req.sheet() != null ? req.sheet() : new HashMap<>());
        character.setTemplateVersion(gameSystem.getTemplateVersion());

        gameCharacterRepository.save(character);

        return toResponse(character);
    }

    @Transactional(readOnly = true)
    public List<CharacterResponse> list(Long userId) {
        List<GameCharacterEntity> characters = gameCharacterRepository.findByAppUserIdOrderByCreatedAtDesc(userId);
        return characters.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CharacterResponse get(Long userId, Long characterId) {
        GameCharacterEntity gameCharacterEntity = getCharacter(userId, characterId);
        return toResponse(gameCharacterEntity);
    }

    @Transactional
    public CharacterResponse update(Long userId, Long characterId, UpdateCharacterRequest req) {
        GameCharacterEntity gameCharacterEntity = getCharacter(userId, characterId);
        gameCharacterEntity.setName(req.name());
        gameCharacterEntity.setSheet(req.sheet());

        return toResponse(gameCharacterEntity);
    }



    @Transactional
    public CharacterResponse join(Long userId, Long characterId, String rawCode) {
        GameCharacterEntity gameCharacterEntity = gameCharacterRepository.findByIdAndAppUserId(characterId, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found"));
        String code = rawCode.trim().replace(" ", "").replace("-", "").toUpperCase(Locale.ROOT);
        GameInviteEntity gameInviteEntity = gameInviteRepository.findByCodeAndRevokedAtIsNull(code)
                .filter(i -> i.getExpiresAt().isAfter(OffsetDateTime.now()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expired or invalid invite code"));
        if(!Objects.equals(gameInviteEntity.getGame().getGameSystem().getId(), gameCharacterEntity.getGameSystem().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GameSystem does not match");
        }
        if(gameCharacterEntity.getMembership() != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Character already in another game");
        }
        MembershipEntity membership = membershipRepository
                .findByAppUserIdAndGameId(userId, gameInviteEntity.getGame().getId())
                .orElseGet(() -> {
                    MembershipEntity m = new MembershipEntity();
                    m.setAppUser(gameCharacterEntity.getAppUser());
                    m.setGame(gameInviteEntity.getGame());
                    m.setPlayerRole(playerRoleRepository.findByRole(PlayerRoleType.PLAYER)
                            .orElseThrow(() -> new IllegalStateException("Role PLAYER chybí v databázi")));
                    return membershipRepository.save(m);
                });

        gameCharacterEntity.setMembership(membership);
        return toResponse(gameCharacterEntity);
    }

    @Transactional
    public CharacterResponse leave(Long userId, Long characterId) {
        GameCharacterEntity c = gameCharacterRepository.findByIdAndAppUserId(characterId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found"));
        c.setMembership(null);
        return toResponse(c);
    }

    private GameCharacterEntity getCharacter(Long userId, Long charId){
        return gameCharacterRepository.findByIdAndAppUserId(charId, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown character"));
    }

    private CharacterResponse toResponse(GameCharacterEntity c) {
        MembershipEntity m = c.getMembership();
        return new CharacterResponse(
                c.getId(),
                c.getName(),
                c.getGameSystem().getCode(),
                c.getTemplateVersion(),
                c.getSheet(),
                m == null ? null : m.getGame().getId(),
                m == null ? null : m.getGame().getName());
    }
}
