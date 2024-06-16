package de.ait_tr.g_40_shop.exception_handling.exceptions;

public class InvalidConfirmationCodeException extends RuntimeException{
    public InvalidConfirmationCodeException(String message) {
        super(message);
    }
}
