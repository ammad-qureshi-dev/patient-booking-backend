package com.patient_booking.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Turns exceptions into RFC 9457 problem responses.
 * User errors -> 400 with the reason; anything unexpected -> 500 with a generic message.
 * The parent class
 * handles Spring MVC's own errors (malformed JSON, bad path variable, wrong HTTP method) with their standard status codes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String SYSTEM_ERROR_MESSAGE = "An unexpected error occurred";

    @ExceptionHandler(UserException.class)
    public ProblemDetail handleUserException(UserException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSystemException(Exception ex) {
        // Log the real cause; never leak internals to the client.
        log.error("Unhandled exception", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, SYSTEM_ERROR_MESSAGE);
    }

    /** Bean validation failures on @Valid request bodies: list each invalid field. */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> String.valueOf(fieldError.getDefaultMessage()),
                        (first, second) -> first));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setProperty("errors", fieldErrors);
        return ResponseEntity.badRequest().body(problem);
    }
}
