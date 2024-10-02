package com.zim0101.authvault.exception;

public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException() {
        super("Your verification token has expired!");
    }
}