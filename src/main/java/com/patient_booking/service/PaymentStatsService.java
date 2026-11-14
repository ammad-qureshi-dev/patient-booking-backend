package com.patient_booking.service;

import com.patient_booking.controllers.response.PaymentStatsResponse;
import com.patient_booking.controllers.response.PaymentTotals;
import com.patient_booking.controllers.response.PaymentYearRecapResponse;
import com.patient_booking.models.DateRange;
import com.patient_booking.models.enums.PaymentType;
import com.patient_booking.repositories.PaymentRepository;
import com.patient_booking.repositories.projections.PaymentDailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

/** Read-only finance reporting over received payments (grouped by the day money was received). */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentStatsService {

    private final PaymentRepository paymentRepository;

    public PaymentStatsResponse getTodayStats() {
        return getStats(DateRange.ofDay(LocalDate.now()));
    }

    public PaymentStatsResponse getMonthStats(YearMonth month) {
        return getStats(DateRange.ofMonth(month));
    }

    /** Folds the per-(day, type) rows into overall, per-type and per-day totals in one pass. */
    public PaymentStatsResponse getStats(DateRange range) {
        PaymentTotals total = PaymentTotals.EMPTY;
        Map<PaymentType, PaymentTotals> byPaymentType = emptyTotalsPerType();
        Map<LocalDate, PaymentTotals> byDay = new TreeMap<>();

        for (PaymentDailySummary row : paymentRepository.summarizeReceivedBetween(
                range.startInclusive(), range.endExclusive())) {
            PaymentTotals rowTotals = PaymentTotals.of(row.getCount(), row.getAmountReceived(), row.getTipAmount());
            total = total.plus(rowTotals);
            byPaymentType.merge(row.getType(), rowTotals, PaymentTotals::plus);
            byDay.merge(row.getDay(), rowTotals, PaymentTotals::plus);
        }

        return new PaymentStatsResponse(range.fromDate(), range.toDate(), total, byPaymentType, byDay);
    }

    /** Same per-(day, type) rows as getStats, folded into per-month totals instead of per-day. */
    public PaymentYearRecapResponse getYearRecap(Year year) {
        DateRange range = DateRange.ofYear(year);
        PaymentTotals total = PaymentTotals.EMPTY;
        Map<PaymentType, PaymentTotals> byPaymentType = emptyTotalsPerType();
        Map<YearMonth, PaymentTotals> byMonth = new TreeMap<>();
        for (Month month : Month.values()) {
            byMonth.put(year.atMonth(month), PaymentTotals.EMPTY);
        }

        for (PaymentDailySummary row : paymentRepository.summarizeReceivedBetween(
                range.startInclusive(), range.endExclusive())) {
            PaymentTotals rowTotals = PaymentTotals.of(row.getCount(), row.getAmountReceived(), row.getTipAmount());
            total = total.plus(rowTotals);
            byPaymentType.merge(row.getType(), rowTotals, PaymentTotals::plus);
            byMonth.merge(YearMonth.from(row.getDay()), rowTotals, PaymentTotals::plus);
        }

        YearMonth busiestMonth = byMonth.entrySet().stream()
                .filter(entry -> entry.getValue().paymentCount() > 0)
                .max(Comparator.comparing(entry -> entry.getValue().withTips()))
                .map(Map.Entry::getKey)
                .orElse(null);

        return new PaymentYearRecapResponse(year.getValue(), total, byPaymentType, byMonth, busiestMonth);
    }

    private Map<PaymentType, PaymentTotals> emptyTotalsPerType() {
        Map<PaymentType, PaymentTotals> totals = new EnumMap<>(PaymentType.class);
        for (PaymentType type : PaymentType.values()) {
            totals.put(type, PaymentTotals.EMPTY);
        }
        return totals;
    }
}
