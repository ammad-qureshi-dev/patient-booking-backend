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
     * Totals of payments received in [from, to), grouped by day, payment type and appointment length.
     * Pending payments (receivedAt is null) are excluded by the range filter.
     * Payments with hoursWorked set are summed into manualHours; the rest are counted in
     * bookedCount so their hours can be derived from the appointment length.
     */
    @Query("""
            SELECT CAST(p.receivedAt AS LocalDate) AS day,
                   p.type AS type,
                   a.length AS length,
                   COUNT(p) AS count,
                   SUM(p.amountReceived) AS amountReceived,
                   SUM(p.tipAmount) AS tipAmount,
                   SUM(p.hoursWorked) AS manualHours,
                   SUM(CASE WHEN p.hoursWorked IS NULL THEN 1 ELSE 0 END) AS bookedCount
            FROM Payment p
            LEFT JOIN Appointment a ON a.id = p.appointmentId
            WHERE p.receivedAt >= :from AND p.receivedAt < :to
            GROUP BY CAST(p.receivedAt AS LocalDate), p.type, a.length
            """)
    List<PaymentDailySummary> summarizeReceivedBetween(@Param("from") LocalDateTime from,
                                                       @Param("to") LocalDateTime to);
}
