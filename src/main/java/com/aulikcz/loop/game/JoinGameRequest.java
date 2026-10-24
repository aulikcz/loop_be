package com.aulikcz.loop.game;

import jakarta.validation.constraints.NotBlank;

public record JoinGameRequest(@NotBlank String code) {}