package com.patient_booking.exceptions;

/** Thrown when a request is well-formed but breaks a booking rule (e.g. off-slot start time). */
public class InvalidAppointmentException extends UserException {

    public InvalidAppointmentException(String message) {
        super(message);
    }
}
