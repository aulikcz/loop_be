package com.aulikcz.loop.gamesystem;

public record GameSystemDto(Long id, String code, String name, Integer templateVersion) {
    public static GameSystemDto from(GameSystemEntity entity) {
        return new GameSystemDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getTemplateVersion()
        );
    }
}