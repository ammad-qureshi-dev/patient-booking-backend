package com.patient_booking.controllers;

import com.patient_booking.controllers.response.PaymentStatsResponse;
import com.patient_booking.controllers.response.PaymentYearRecapResponse;
import com.patient_booking.models.DateRange;
import com.patient_booking.service.PaymentStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

@RestController
@RequestMapping("/v1/payment/stats")
@RequiredArgsConstructor
public class PaymentStatsController {

    private final PaymentStatsService paymentStatsService;

    @GetMapping("/today")
    public ResponseEntity<PaymentStatsResponse> getTodayStats() {
        return ResponseEntity.ok(paymentStatsService.getTodayStats());
    }

    @GetMapping("/month")
    public ResponseEntity<PaymentStatsResponse> getCurrentMonthStats() {
        return ResponseEntity.ok(paymentStatsService.getMonthStats(YearMonth.now()));
    }

    /** Month in yyyy-MM format, e.g. /month/2026-11. */
    @GetMapping("/month/{month}")
    public ResponseEntity<PaymentStatsResponse> getMonthStats(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(paymentStatsService.getMonthStats(month));
    }

    @GetMapping("/year")
    public ResponseEntity<PaymentYearRecapResponse> getCurrentYearRecap() {
        return ResponseEntity.ok(paymentStatsService.getYearRecap(Year.now()));
    }

    /** Year in yyyy format, e.g. /year/2026. */
    @GetMapping("/year/{year}")
    public ResponseEntity<PaymentYearRecapResponse> getYearRecap(
            @PathVariable Year year) {
        return ResponseEntity.ok(paymentStatsService.getYearRecap(year));
    }

    /** Dates are ISO format (yyyy-MM-dd) and both inclusive. */
    @GetMapping("/from/{fromDate}/to/{toDate}")
    public ResponseEntity<PaymentStatsResponse> getStatsBetween(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(paymentStatsService.getStats(new DateRange(fromDate, toDate)));
    }
}
