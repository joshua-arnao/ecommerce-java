package com.ecommerce.sportscenter.shared.exceptions;

import com.ecommerce.sportscenter.shared.error.CustomerErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handlerProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        CustomerErrorResponse error = new CustomerErrorResponse(
                404,
                "Product doesn't exist",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handlerAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        CustomerErrorResponse error = new CustomerErrorResponse(
                400,
                "Bad Request",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        CustomerErrorResponse error = new CustomerErrorResponse(
                400,
                "Invalid credentials",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        CustomerErrorResponse error = new CustomerErrorResponse(
                400,
                "Invalid credentials",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<Object> handleShoppingCartNotFoundException(ShoppingCartNotFoundException ex, WebRequest request) {
        CustomerErrorResponse error = new CustomerErrorResponse(
                404,
                "Shopping cart not found",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        if (ex.getMessage() != null && ex.getMessage().contains("swagger")) {
            throw new RuntimeException(ex);
        }
        CustomerErrorResponse error = new CustomerErrorResponse(
                500,
                "An error occurred",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );
        return ResponseEntity.status(500).body(error);
    }
}
