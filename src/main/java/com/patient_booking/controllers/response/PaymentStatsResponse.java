package com.patient_booking.controllers.response;

import com.patient_booking.models.enums.PaymentType;

import java.time.LocalDate;
import java.util.Map;

/**
 * Finance summary for a date range.
 * byPaymentType always lists every type (zeros if unused); byDay only lists days with payments.
 */
public record PaymentStatsResponse(
        LocalDate fromDate,
        LocalDate toDate,
        PaymentTotals total,
        Map<PaymentType, PaymentTotals> byPaymentType,
        Map<LocalDate, PaymentTotals> byDay
) {
}
