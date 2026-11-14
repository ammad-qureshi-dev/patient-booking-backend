package com.patient_booking.repositories;

import com.patient_booking.models.Payment;
import com.patient_booking.repositories.projections.PaymentDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByAppointmentId(UUID appointmentId);

    /**
     * Totals of payments received in [from, to), grouped by day and payment type.
     * Pending payments (receivedAt is null) are excluded by the range filter.
     */
    @Query("""
            SELECT CAST(p.receivedAt AS LocalDate) AS day,
                   p.type AS type,
                   COUNT(p) AS count,
                   SUM(p.amountReceived) AS amountReceived,
                   SUM(p.tipAmount) AS tipAmount
            FROM Payment p
            WHERE p.receivedAt >= :from AND p.receivedAt < :to
            GROUP BY CAST(p.receivedAt AS LocalDate), p.type
            """)
    List<PaymentDailySummary> summarizeReceivedBetween(@Param("from") LocalDateTime from,
                                                       @Param("to") LocalDateTime to);
}
