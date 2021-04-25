package io.springbytes.exception;

public class InvalidPocketException extends RuntimeException {

	private static final long serialVersionUID = -1805676789736773413L;

	public InvalidPocketException(String message) {
        super(message);
    }
}
