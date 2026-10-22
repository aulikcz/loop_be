package com.aulikcz.loop.user;

public record UserDto(Long id, String displayName, String email) {
    public static UserDto from(AppUserEntity entity) {
        return new UserDto(
            entity.getId(),
            entity.getDisplayName(),
            entity.getEmail()
        );
    }
}
