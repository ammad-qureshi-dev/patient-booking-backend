package com.patient_booking.service;

import com.patient_booking.controllers.request.PaymentRequest;
import com.patient_booking.exceptions.PaymentNotFoundException;
import com.patient_booking.models.Payment;
import com.patient_booking.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /** Called when an appointment is booked: nothing received yet. */
    public void createPendingPayment(UUID appointmentId) {
        Payment payment = Payment.builder()
                .appointmentId(appointmentId)
                .amountReceived(BigDecimal.ZERO)
                .tipAmount(BigDecimal.ZERO)
                .build();
        paymentRepository.save(payment);
    }

    /** Called by the owner after the service is done. */
    public UUID recordPayment(UUID appointmentId, PaymentRequest request) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new PaymentNotFoundException(appointmentId));

        payment.setAmountReceived(request.amountReceived());
        payment.setTipAmount(Objects.requireNonNullElse(request.tipAmount(), BigDecimal.ZERO));
        payment.setType(request.type());
        payment.setReceivedAt(LocalDateTime.now());
        // Managed entity: changes are flushed on commit.
        return payment.getId();
    }
}
