package com.patient_booking.exceptions;

import java.time.LocalDate;

public class InvalidDateRangeException extends UserException {

    public InvalidDateRangeException(LocalDate fromDate, LocalDate toDate) {
        super("fromDate " + fromDate + " must not be after toDate " + toDate);
    }
}
