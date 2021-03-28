package com.example.demo.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String token_not_found) {
        super(token_not_found);
    }
}
