package ru.duhov.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CalculatorExceptionHandler {

    @ExceptionHandler(value = {CreditRefusedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleCreditRefusalException(CreditRefusedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
