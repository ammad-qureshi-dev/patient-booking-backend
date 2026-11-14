package com.patient_booking.controllers;

import com.patient_booking.controllers.request.AppointmentRequest;
import com.patient_booking.controllers.response.AppointmentResponse;
import com.patient_booking.models.DateRange;
import com.patient_booking.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<UUID> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        UUID appointmentId = appointmentService.createAppointment(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{appointmentId}")
                .buildAndExpand(appointmentId)
                .toUri();
        return ResponseEntity.created(location).body(appointmentId);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/today")
    public ResponseEntity<List<AppointmentResponse>> getTodayAppointments() {
        return ResponseEntity.ok(appointmentService.getTodayAppointments());
    }

    @GetMapping("/tomorrow")
    public ResponseEntity<List<AppointmentResponse>> getTomorrowAppointments() {
        return ResponseEntity.ok(appointmentService.getTomorrowAppointments());
    }

    /** Dates are ISO format (yyyy-MM-dd) and both inclusive. */
    @GetMapping("/from/{fromDate}/to/{toDate}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsBetween(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsBetween(new DateRange(fromDate, toDate)));
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<UUID> updateAppointment(@PathVariable UUID appointmentId,
                                                  @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentId, request));
    }
}
