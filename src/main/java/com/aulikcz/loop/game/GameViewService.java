package com.aulikcz.loop.game;

import com.aulikcz.loop.character.GameCharacterEntity;
import com.aulikcz.loop.character.GameCharacterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GameViewService {

    private final GameCharacterRepository gameCharacterRepository;
    private final MembershipRepository membershipRepository;

    public GameViewService(GameCharacterRepository gameCharacterRepository,
                           MembershipRepository membershipRepository) {
        this.gameCharacterRepository = gameCharacterRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public GameInfo game(Long userId, Long gameId) {
        MembershipEntity m = requireMembership(userId, gameId);
        GameEntity g = m.getGame();
        return new GameInfo(g.getId(), g.getName(), g.getGameSystem().getName(),
                g.getGameSystem().getCode(), m.getPlayerRole().getRole().name());
    }

    @Transactional(readOnly = true)
    public List<GameCharacterListItem> characters(Long userId, Long gameId) {
        MembershipEntity m = requireMembership(userId, gameId);
        List<GameCharacterEntity> characters = isNarrator(m)
                ? gameCharacterRepository.findByMembershipGameIdOrderByNameAsc(gameId)
                : gameCharacterRepository.findByMembershipGameIdAndAppUserIdOrderByNameAsc(gameId, userId);
        return characters.stream()
                .map(c -> new GameCharacterListItem(
                        c.getId(),
                        c.getName(),
                        c.getGameSystem().getCode(),
                        c.getAppUser().getDisplayName(),
                        c.getAppUser().getId().equals(userId)))
                .toList();
    }

    @Transactional(readOnly = true)
    public GameCharacterDetail character(Long userId, Long gameId, Long characterId) {
        MembershipEntity m = requireMembership(userId, gameId);
        GameCharacterEntity c = gameCharacterRepository.findByIdAndMembershipGameId(characterId, gameId)
                .orElseThrow(this::notFound);
        boolean owner = c.getAppUser().getId().equals(userId);
        if (!owner && !isNarrator(m)) {
            throw notFound();
        }
        return new GameCharacterDetail(c.getId(), c.getName(), c.getGameSystem().getCode(),
                c.getTemplateVersion(), c.getSheet(), c.getAppUser().getDisplayName(), owner);
    }

    private MembershipEntity requireMembership(Long userId, Long gameId) {
        return membershipRepository.findByAppUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    private boolean isNarrator(MembershipEntity m) {
        return m.getPlayerRole().getRole() == PlayerRoleType.NARRATOR;
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }
}
