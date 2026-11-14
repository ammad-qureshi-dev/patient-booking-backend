package com.patient_booking.controllers.response;

import com.patient_booking.models.enums.PaymentType;

import java.time.YearMonth;
import java.util.Map;

/**
 * Year-end finance recap.
 * byPaymentType always lists every type and byMonth always lists all 12 months (zeros if unused).
 * busiestMonth is the month with the highest withTips total, or null if the year has no payments.
 */
public record PaymentYearRecapResponse(
        int year,
        PaymentTotals total,
        Map<PaymentType, PaymentTotals> byPaymentType,
        Map<YearMonth, PaymentTotals> byMonth,
        YearMonth busiestMonth
) {
}
