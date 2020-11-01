package com.rmaj91.security.exceptions;

public class IncorrectUsernamePasswordException extends RuntimeException {

    public IncorrectUsernamePasswordException() {
        super("Incorrect password or username.");
    }
}
