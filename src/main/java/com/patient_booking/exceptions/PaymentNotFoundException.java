package com.patient_booking.exceptions;

import java.util.UUID;

public class PaymentNotFoundException extends UserException {

    public PaymentNotFoundException(UUID appointmentId) {
        super("Payment not found for appointment: " + appointmentId);
    }
}
