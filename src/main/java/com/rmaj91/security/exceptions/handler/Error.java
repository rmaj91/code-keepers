package com.rmaj91.security.exceptions.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {

    private String errorPayload;

    private Error(String errorPayload) {
        this.errorPayload = errorPayload;
    }

    public static Error withMessage(String message) {
        return new Error(message);
    }
}
