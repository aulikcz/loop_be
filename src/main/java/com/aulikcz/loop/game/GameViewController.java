package com.aulikcz.loop.game;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games/{gameId}")
public class GameViewController {

    private final GameViewService gameViewService;

    public GameViewController(GameViewService gameViewService) {
        this.gameViewService = gameViewService;
    }

    @GetMapping
    public GameInfo game(@AuthenticationPrincipal Jwt jwt, @PathVariable Long gameId) {
        return gameViewService.game(Long.valueOf(jwt.getSubject()), gameId);
    }

    @GetMapping("/characters")
    public List<GameCharacterListItem> characters(@AuthenticationPrincipal Jwt jwt, @PathVariable Long gameId) {
        return gameViewService.characters(Long.valueOf(jwt.getSubject()), gameId);
    }

    @GetMapping("/characters/{characterId}")
    public GameCharacterDetail character(@AuthenticationPrincipal Jwt jwt, @PathVariable Long gameId,
                                         @PathVariable Long characterId) {
        return gameViewService.character(Long.valueOf(jwt.getSubject()), gameId, characterId);
    }
}
