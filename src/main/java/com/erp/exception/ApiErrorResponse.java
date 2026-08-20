package com.erp.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardised error response returned by GlobalExceptionHandler.
 * Clients always get a consistent JSON shape regardless of error type.
 */
public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    // For validation errors we also return per-field details
    private List<String> fieldErrors;

    public ApiErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(int status, String error, String message, List<String> fieldErrors) {
        this(status, error, message);
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<String> getFieldErrors() { return fieldErrors; }
}
