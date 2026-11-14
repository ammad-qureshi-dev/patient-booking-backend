package com.patient_booking.models;

import com.patient_booking.models.enums.AppointmentLength;
import com.patient_booking.models.enums.AppointmentStatus;
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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "appointment_service", name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String fullName;
    private String notes;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AppointmentLength length;

    @NotNull
    private LocalDateTime startTime;

    /** Derived from startTime + length; stored so overlap queries stay simple. */
    @NotNull
    private LocalDateTime endTime;
}
