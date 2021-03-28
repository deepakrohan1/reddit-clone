package com.example.demo.exception;

public class MailSenderApplicationException extends RuntimeException {
    public MailSenderApplicationException(String s) {
        super(s);
    }
}
