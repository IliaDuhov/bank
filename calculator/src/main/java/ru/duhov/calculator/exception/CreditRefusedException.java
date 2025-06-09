package ru.duhov.calculator.exception;

public class CreditRefusedException extends RuntimeException{
    public CreditRefusedException() {
    }

    public CreditRefusedException(String message) {
        super(message);
    }

    public CreditRefusedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditRefusedException(Throwable cause) {
        super(cause);
    }

    public CreditRefusedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
