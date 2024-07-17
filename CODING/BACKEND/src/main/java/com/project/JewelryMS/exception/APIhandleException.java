package com.project.JewelryMS.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class APIhandleException {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleInvalidUsernamePassword(BadCredentialsException ex){
        return new ResponseEntity<>("Username or password not correct", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Object> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleDuplicate(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage().toLowerCase();

        if (message.contains("account.UKd3xjtwxrpxnpf7j2x5f35kdwo")) {
            return handleDuplicateUsernameException(new DuplicateUsernameException("Username đã được sử dụng"));
        } else if (message.contains("account.UKcs5bnaggwuluahrdh8mbs1rpe")) {
            return handleDuplicateEmailException(new DuplicateEmailException("Email đã được sử dụng."));
        }

        return new ResponseEntity<>("Duplicate", HttpStatus.BAD_REQUEST);
    }

}
