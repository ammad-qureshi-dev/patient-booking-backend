package com.patient_booking.exceptions;

import java.util.UUID;

public class AppointmentNotFoundException extends UserException {

    public AppointmentNotFoundException(UUID appointmentId) {
        super("Appointment not found: " + appointmentId);
    }
}
