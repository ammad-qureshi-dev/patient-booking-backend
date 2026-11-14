package com.patient_booking.controllers;

import com.patient_booking.controllers.request.PaymentRequest;
import com.patient_booking.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** A payment belongs to an appointment, so it's addressed through the appointment's id. */
@RestController
@RequestMapping("/v1/appointment/{appointmentId}/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PutMapping
    public ResponseEntity<UUID> recordPayment(@PathVariable UUID appointmentId,
                                              @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.recordPayment(appointmentId, request));
    }
}
