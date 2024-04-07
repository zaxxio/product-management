package org.wsd.app.controller.hanlder;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
        return ResponseEntity.internalServerError()
                .body(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.internalServerError()
                .body(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {CommandExecutionException.class})
    public ResponseEntity<?> handleCommandExecutionException(CommandExecutionException exception, WebRequest webRequest) {
        return ResponseEntity.internalServerError()
                .body(exception.getMessage());
    }

}
