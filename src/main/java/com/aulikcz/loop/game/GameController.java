package com.aulikcz.loop.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@Slf4j
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GameDto createGame(@RequestBody CreateGameRequest createGameRequest) {
        log.info("Creating new game {}", createGameRequest.name());
        return gameService.create(createGameRequest);
    }

    @GetMapping
    public List<GameListItemDto> getPlayerGamesList(){
        log.info("Loading games for user");
        return gameService.getUserGames();
    }
}
