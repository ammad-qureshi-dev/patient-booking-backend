package com.patient_booking.repositories;

import com.patient_booking.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findAllByOrderByStartTimeAsc();

    /** Appointments starting in the half-open window [from, to). */
    @Query("""
            SELECT a FROM Appointment a
            WHERE a.startTime >= :from AND a.startTime < :to
            ORDER BY a.startTime ASC
            """)
    List<Appointment> findAllStartingBetween(@Param("from") LocalDateTime from,
                                             @Param("to") LocalDateTime to);

    /**
     * True if a confirmed appointment overlaps [start, end).
     * Pass excludeId when updating so the appointment doesn't clash with itself; null when creating.
     */
    @Query("""
            SELECT COUNT(a) > 0 FROM Appointment a
            WHERE a.status = com.patient_booking.models.enums.AppointmentStatus.CONFIRMED
              AND a.startTime < :end
              AND a.endTime > :start
              AND (:excludeId IS NULL OR a.id <> :excludeId)
            """)
    boolean existsOverlapping(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("excludeId") UUID excludeId);
}
