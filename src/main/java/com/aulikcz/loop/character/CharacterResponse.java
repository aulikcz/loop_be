package com.aulikcz.loop.character;

import java.util.Map;

public record CharacterResponse(
        Long id,
        String name,
        String systemCode,
        Integer templateVersion,
        Map<String, Object> sheet,
        Long gameId,
        String gameName
) {}
