package com.rmaj91.security.exceptions.handler;

import com.rmaj91.security.exceptions.IncorrectUsernamePasswordException;
import com.rmaj91.security.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IncorrectUsernamePasswordException.class)
    public Error incorrectUsernameOrPasswordException(IncorrectUsernamePasswordException exception) {
        return Error.withMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public Error usernameAlreadyTakenException(UsernameAlreadyTakenException exception) {
        return Error.withMessage(exception.getMessage());
    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = {ExpiredJwtException.class, UnsupportedJwtException.class, MalformedJwtException.class, SignatureException.class})
//    public Error jwtException(RuntimeException exception) {
//        return Error.withMessage(exception.getMessage());
//    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {RuntimeException.class,})
    public Error authenticationException(AuthenticationException exception) {
        return Error.withMessage(exception.getMessage());
    }
}
