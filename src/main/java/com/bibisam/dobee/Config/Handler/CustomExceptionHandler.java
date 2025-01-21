package com.bibisam.dobee.Config.Handler;

import com.bibisam.dobee.Exceptions.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException() {
        System.out.println("핸들러안에드러옴..unauthorized..");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("hello custom");
    }
}
