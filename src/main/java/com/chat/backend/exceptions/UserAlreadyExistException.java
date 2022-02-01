package com.chat.backend.exceptions;

public class UserAlreadyExistException extends RuntimeException  {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
