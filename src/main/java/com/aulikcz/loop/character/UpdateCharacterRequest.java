package com.aulikcz.loop.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record UpdateCharacterRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull Map<String, Object> sheet
) {}