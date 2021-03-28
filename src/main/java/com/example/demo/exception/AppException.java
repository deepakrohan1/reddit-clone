package com.example.demo.exception;

public class AppException extends RuntimeException {
    public AppException(String exception_occured_while_loading_keystore) {
        super(exception_occured_while_loading_keystore);
    }
}
