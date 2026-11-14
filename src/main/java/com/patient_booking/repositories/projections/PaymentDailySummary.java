package com.patient_booking.repositories.projections;

import com.patient_booking.models.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One aggregated row per (day, payment type) of received payments. */
public interface PaymentDailySummary {

    LocalDate getDay();

    PaymentType getType();

    long getCount();

    BigDecimal getAmountReceived();

    BigDecimal getTipAmount();
}
