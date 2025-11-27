package com.qk.mscliente.exception;

import com.qk.mscliente.dto.ApiSubError;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final transient List<ApiSubError> subErrors;

    public ValidationException(String message, List<ApiSubError> subErrors) {
        super(message);
        this.subErrors = subErrors;
    }

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }
}
