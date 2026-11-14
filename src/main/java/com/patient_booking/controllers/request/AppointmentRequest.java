package com.patient_booking.controllers.request;

import com.patient_booking.models.enums.AppointmentLength;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotBlank @Size(max = 255) String fullName,
        @Size(max = 2000) String notes,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        @NotNull @Future LocalDateTime startTime,
        @NotNull AppointmentLength length
) {
}
