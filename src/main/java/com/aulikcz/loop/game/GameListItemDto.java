package com.aulikcz.loop.game;

public record GameListItemDto(Long id, String name, String gameSystem, PlayerRoleType playerRole) {
    public static GameListItemDto from(GameEntity game, MembershipEntity membership) {
        return new GameListItemDto(
                game.getId(),
                game.getName(),
                game.getGameSystem().getName(),
                membership.getPlayerRole().getRole()
        );
    }
}