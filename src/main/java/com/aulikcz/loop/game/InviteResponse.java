package com.aulikcz.loop.game;

import java.time.OffsetDateTime;

public record InviteResponse(String code, OffsetDateTime expiresAt) {}