package com.eincrm.exception;

public class UserNotConfirmedException extends RuntimeException {
    public UserNotConfirmedException(String message) {
        super(message);
    }
}