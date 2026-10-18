package com.aulikcz.loop.game;

public record GameDto(Long id, String name, String gameSystemCode, String createdByName) {
    public static GameDto from(GameEntity entity) {
        return new GameDto(
                entity.getId(),
                entity.getName(),
                entity.getGameSystem().getCode(),
                entity.getCreatedBy().getDisplayName()
        );
    }
}
