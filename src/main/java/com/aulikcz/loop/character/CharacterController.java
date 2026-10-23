package com.aulikcz.loop.character;

import com.aulikcz.loop.common.BadLogin;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService){
        this.characterService = characterService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterResponse create(@AuthenticationPrincipal Jwt jwt,
                                    @Valid @RequestBody CreateCharacterRequest req) {
        Long userId = Long.valueOf(jwt.getSubject());
        return characterService.create(userId, req);
    }

    @GetMapping
    public List<CharacterResponse> getUserCharacters(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return characterService.list(userId);
    }

    @GetMapping("/{id}")
    public CharacterResponse getCharacterDetail(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        Long userId = Long.valueOf(jwt.getSubject());
        return characterService.get(userId, id);
    }

    @PutMapping("/{id}")
    public CharacterResponse update(@AuthenticationPrincipal Jwt jwt,
                                    @PathVariable Long id,
                                    @Valid @RequestBody UpdateCharacterRequest req) {
        Long userId = Long.valueOf(jwt.getSubject());
        return characterService.update(userId, id, req);
    }
}
