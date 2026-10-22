package com.aulikcz.loop.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank @Email @Size(max = 100)String email, @NotBlank @Size(min = 8, max = 128) String password, @NotBlank @Size(min = 2, max = 30) String displayName) {
}
