package com.aulikcz.loop.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Not all inputs filled")
public class InputValueMissingException extends RuntimeException {
    public InputValueMissingException(String message) {
        super(message);
    }
}
