package com.patient_booking.controllers.response;

import com.patient_booking.models.Appointment;
import com.patient_booking.models.enums.AppointmentLength;
import com.patient_booking.models.enums.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String fullName,
        String notes,
        BigDecimal price,
        AppointmentStatus status,
        AppointmentLength length,
        LocalDateTime startTime,
        LocalDateTime endTime
) {

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getFullName(),
                appointment.getNotes(),
                appointment.getPrice(),
                appointment.getStatus(),
                appointment.getLength(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
    }
}
