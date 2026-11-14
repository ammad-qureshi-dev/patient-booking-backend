package com.patient_booking.controllers.request;

import com.patient_booking.models.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** tipAmount is optional; a missing tip is stored as 0. */
public record PaymentRequest(
        @NotNull @DecimalMin("0.00") BigDecimal amountReceived,
        @DecimalMin("0.00") BigDecimal tipAmount,
        @NotNull PaymentType type
) {
}
