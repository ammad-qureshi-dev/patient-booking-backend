package com.patient_booking.models;

import com.patient_booking.models.enums.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Financial record for one appointment.
 * Created as a pending record (amount 0) when the appointment is booked, then filled in
 * by the owner once the service is done. receivedAt and type stay null until then.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "appointment_service", name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Plain column rather than a JPA relationship; one payment per appointment. */
    @NotNull
    @Column(nullable = false, unique = true)
    private UUID appointmentId;

    @NotNull
    private BigDecimal amountReceived;

    @NotNull
    private BigDecimal tipAmount;

    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    private PaymentType type;
}
