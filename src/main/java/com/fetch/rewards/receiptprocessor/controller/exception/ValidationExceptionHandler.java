package com.fetch.rewards.receiptprocessor.controller.exception;

import com.fetch.rewards.receiptprocessor.model.ReceiptResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller Advisor to handle different exceptions
 */
@ControllerAdvice
public class ValidationExceptionHandler {

    /**
     * Handle exception related to Invalid Input such as empty retailer name, negative prices and so on
     * Refer to the model class.
     * @param ex Validation on arguments with @Valid annotation
     * @return Response object with error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(final MethodArgumentNotValidException ex) {
        final List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        return new ResponseEntity<>(ReceiptResponse.builder().errors(errors).build(),
                                    HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle exception related to Local Date and Local Time
     * @param ex corresponds to malformed JSON, incorrect content-type, empty response etc
     * @return Response object with error messages
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseException(HttpMessageNotReadableException ex) {
        // Handle JSON parse errors
        final List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        return new ResponseEntity<>(ReceiptResponse.builder().errors(errors).build(),
                                    HttpStatus.BAD_REQUEST);
    }
}
