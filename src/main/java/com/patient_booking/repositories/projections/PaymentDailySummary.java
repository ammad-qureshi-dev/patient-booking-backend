package com.patient_booking.repositories.projections;

import com.patient_booking.models.enums.AppointmentLength;
import com.patient_booking.models.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One aggregated row per (day, payment type, appointment length) of received payments. */
public interface PaymentDailySummary {

    LocalDate getDay();

    PaymentType getType();

    long getCount();

    BigDecimal getAmountReceived();

    BigDecimal getTipAmount();

    /** Null when the payment has no matching appointment. */
    AppointmentLength getLength();

    /** Sum of explicit hoursWorked; null when no payment in the group has it set. */
    BigDecimal getManualHours();

    /** Payments without explicit hoursWorked, whose hours come from the appointment length. */
    long getBookedCount();
}
