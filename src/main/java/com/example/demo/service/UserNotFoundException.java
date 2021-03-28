package com.example.demo.service;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String no_valid_user_found) {
        super(no_valid_user_found);
    }
}
