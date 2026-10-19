package com.aulikcz.loop.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Unauthorized")
public class BadLogin extends RuntimeException {
    public BadLogin(String message) {
        super(message);
    }
}
