package com.vasylenkob.pastebin.exceptions;

public class PostDoesNotExistException extends RuntimeException{
    public PostDoesNotExistException(String message) {
        super(message);
    }
}
