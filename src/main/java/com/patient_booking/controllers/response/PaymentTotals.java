package com.patient_booking.controllers.response;

import java.math.BigDecimal;

/** Money totals for a group of payments. withTips is always beforeTips + tips. */
public record PaymentTotals(
        long paymentCount,
        BigDecimal beforeTips,
        BigDecimal tips,
        BigDecimal withTips
) {

    /** Scale 2 so empty totals serialize as 0.00, matching real money values. */
    private static final BigDecimal ZERO_MONEY = new BigDecimal("0.00");

    public static final PaymentTotals EMPTY = of(0, ZERO_MONEY, ZERO_MONEY);

    public static PaymentTotals of(long paymentCount, BigDecimal beforeTips, BigDecimal tips) {
        return new PaymentTotals(paymentCount, beforeTips, tips, beforeTips.add(tips));
    }

    public PaymentTotals plus(PaymentTotals other) {
        return of(paymentCount + other.paymentCount,
                beforeTips.add(other.beforeTips),
                tips.add(other.tips));
    }
}
