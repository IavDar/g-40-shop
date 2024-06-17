package de.ait_tr.g_40_shop.exception_handling.exceptions;

public class ProductNotActiveException extends RuntimeException{
    public ProductNotActiveException(String message) {
        super(message);
    }
}
