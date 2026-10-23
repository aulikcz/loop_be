package com.aulikcz.loop.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record CreateCharacterRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank String systemCode,
        Map<String, Object> sheet   // může být null
) {}
