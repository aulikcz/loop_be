package com.aulikcz.loop.game;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games/{gameId}/invite")
public class GameInviteController {

    private final GameInviteService gameInviteService;

    public GameInviteController(GameInviteService gameInviteService){
        this.gameInviteService = gameInviteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InviteResponse regenerate(@AuthenticationPrincipal Jwt jwt, @PathVariable Long gameId) {
        Long userId = Long.valueOf(jwt.getSubject());
        return gameInviteService.regenerate(userId, gameId);
    }

    @GetMapping
    public InviteResponse current(@AuthenticationPrincipal Jwt jwt, @PathVariable Long gameId) {
        Long userId = Long.valueOf(jwt.getSubject());
        return gameInviteService.getCurrent(userId, gameId);
    }
}
