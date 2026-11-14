package com.patient_booking.exceptions;

import java.time.LocalDateTime;

public class AppointmentConflictException extends UserException {

    public AppointmentConflictException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Time slot " + startTime + " - " + endTime + " overlaps an existing appointment");
    }
}
