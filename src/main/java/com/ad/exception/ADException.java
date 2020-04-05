package com.ad.exception;

/**
 *
 * ADException
 */
public class ADException extends Exception {
    
    private final String descripcionError;

    public ADException(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public ADException(String descripcionError, Throwable thrwbl) {
        super(thrwbl);
        this.descripcionError = descripcionError;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    @Override
    public String getMessage() {
        return descripcionError;
    }
    
}
