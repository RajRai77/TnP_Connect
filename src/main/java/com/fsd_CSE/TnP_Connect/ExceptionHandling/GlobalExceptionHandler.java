package com.fsd_CSE.TnP_Connect.ExceptionHandling;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.HashMap;

@ControllerAdvice // This annotation makes it a global handler for your entire project.
public class GlobalExceptionHandler {

    /**
     * Handles the custom ResourceNotFoundException.
     * @param ex The exception that was thrown.
     * @return A formatted ResponseEntity with a 404 status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * You can add handlers for other exceptions here later,
     * for example, for handling validation errors.
     */
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    //     // ... logic to handle validation errors ...
    // }


}
