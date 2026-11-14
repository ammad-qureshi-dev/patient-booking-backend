package com.patient_booking.exceptions;

/**
 * Base class for errors caused by the client's request (bad input, unknown id, taken slot).
 * The global handler maps every subclass to 400 Bad Request.
 */
public abstract class UserException extends RuntimeException {

    protected UserException(String message) {
        super(message);
    }
}
