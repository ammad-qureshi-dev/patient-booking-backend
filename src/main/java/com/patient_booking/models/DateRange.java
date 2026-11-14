package com.patient_booking.models;

import com.patient_booking.exceptions.InvalidDateRangeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

/** Calendar date range where both ends are inclusive (whole days). */
public record DateRange(LocalDate fromDate, LocalDate toDate) {

    public DateRange {
        if (fromDate.isAfter(toDate)) {
            throw new InvalidDateRangeException(fromDate, toDate);
        }
    }

    public static DateRange ofDay(LocalDate day) {
        return new DateRange(day, day);
    }

    public static DateRange ofMonth(YearMonth month) {
        return new DateRange(month.atDay(1), month.atEndOfMonth());
    }

    public static DateRange ofYear(Year year) {
        return new DateRange(year.atDay(1), year.atMonth(12).atEndOfMonth());
    }

    /** 00:00 on fromDate. */
    public LocalDateTime startInclusive() {
        return fromDate.atStartOfDay();
    }

    /** 00:00 on the day after toDate, for half-open [start, end) queries. */
    public LocalDateTime endExclusive() {
        return toDate.plusDays(1).atStartOfDay();
    }
}
