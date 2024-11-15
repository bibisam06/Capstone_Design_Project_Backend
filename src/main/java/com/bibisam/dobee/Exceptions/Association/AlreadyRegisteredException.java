package com.bibisam.dobee.Exceptions.Association;

import lombok.Getter;

@Getter
public class AlreadyRegisteredException extends RuntimeException{

    private final int errorCode;
    private final String errorMessage;


    public AlreadyRegisteredException(int errorCode, String errorMessage) {
        this.errorCode = 400;
        this.errorMessage = errorMessage;
    }

}
