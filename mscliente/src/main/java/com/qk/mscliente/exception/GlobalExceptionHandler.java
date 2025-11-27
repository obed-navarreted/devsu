package com.qk.mscliente.exception;

import com.qk.mscliente.dto.ApiSubError;
import com.qk.mscliente.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiSubError(error.getField(), error.getDefaultMessage()))
                .toList();

        String message = "The request has invalid fields";
        ErrorResponse error = prepareErrorObject(((ServletWebRequest) request).getRequest(), message, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SaldoNoDisponibleException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleSaldoInsuficiente(SaldoNoDisponibleException ex, HttpServletRequest request) {
        ErrorResponse error = prepareErrorObject(request, "Saldo no disponible", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        ErrorResponse error = prepareErrorObject(request, e.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleValidationException(ValidationException e, HttpServletRequest request) {
        ErrorResponse error = prepareErrorObject(request, e.getMessage(), e.getSubErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleSQLException(SQLException e, HttpServletRequest request) {
        ErrorResponse error = prepareErrorObject(request, "Database error occurred", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        ErrorResponse error = prepareErrorObject(request, "An unexpected error occurred", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ErrorResponse prepareErrorObject(HttpServletRequest request, String message, Object details) {
        return new ErrorResponse(
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now().toString(),
                message,
                details
        );
    }
}
