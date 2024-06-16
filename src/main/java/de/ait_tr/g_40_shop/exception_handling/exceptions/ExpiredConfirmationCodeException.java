package de.ait_tr.g_40_shop.exception_handling.exceptions;

public class ExpiredConfirmationCodeException extends RuntimeException{
    public ExpiredConfirmationCodeException(String message) {
        super(message);
    }
}
