package com.example.demo.Exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public UserNotFoundException(String message){
        super(message);
    }
}