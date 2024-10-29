package com.vasylenkob.pastebin.exceptions;

public class FailedToSendEmailException extends RuntimeException{
    public FailedToSendEmailException(String message) {
        super(message);
    }
}
