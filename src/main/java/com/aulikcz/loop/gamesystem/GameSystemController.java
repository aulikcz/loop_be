package com.aulikcz.loop.gamesystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game-systems")
@Slf4j
public class GameSystemController {
    private final GameSystemRepository gameSystemRepository;

    public GameSystemController(GameSystemRepository gameSystemRepository){
        this.gameSystemRepository = gameSystemRepository;
    }

    @GetMapping()
    public List<GameSystemDto> getGameSystems() {
        return gameSystemRepository.findAll().stream().map(GameSystemDto::from).toList();
    }
}
