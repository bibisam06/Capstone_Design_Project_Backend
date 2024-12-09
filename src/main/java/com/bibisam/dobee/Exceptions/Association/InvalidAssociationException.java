package com.bibisam.dobee.Exceptions.Association;

public class InvalidAssociationException extends Throwable {
    private final int errorCode;
    private final String errorMessage;


    public InvalidAssociationException(String errorMessage) {
        this.errorCode = 400;
        this.errorMessage = errorMessage;
    }

}
