package io.springbytes.exception;

public class InvalidPocketException extends RuntimeException {

    public InvalidPocketException(String message) {
        super(message);
    }
}
