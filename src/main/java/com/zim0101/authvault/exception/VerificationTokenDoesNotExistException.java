package com.zim0101.authvault.exception;

public class VerificationTokenDoesNotExistException extends RuntimeException {
    public VerificationTokenDoesNotExistException() {
        super("Your verification token does not exist");
    }
}