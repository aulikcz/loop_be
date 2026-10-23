package com.aulikcz.loop.character;

import com.aulikcz.loop.gamesystem.GameSystemEntity;
import com.aulikcz.loop.gamesystem.GameSystemRepository;
import com.aulikcz.loop.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@Service
public class CharacterService {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameSystemRepository gameSystemRepository;
    private final AppUserRepository appUserRepository;

    public CharacterService(GameCharacterRepository gameCharacterRepository, GameSystemRepository gameSystemRepository, AppUserRepository appUserRepository){
        this.gameCharacterRepository = gameCharacterRepository;
        this.gameSystemRepository = gameSystemRepository;
        this.appUserRepository = appUserRepository;
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

    private GameCharacterEntity getCharacter(Long userId, Long charId){
        return gameCharacterRepository.findByIdAndAppUserId(charId, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown character"));
    }

    private CharacterResponse toResponse(GameCharacterEntity c) {
        return new CharacterResponse(
                c.getId(),
                c.getName(),
                c.getGameSystem().getCode(),
                c.getTemplateVersion(),
                c.getSheet());
    }
}
