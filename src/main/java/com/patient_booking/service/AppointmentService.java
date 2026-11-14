package com.patient_booking.service;

import com.patient_booking.controllers.request.AppointmentRequest;
import com.patient_booking.controllers.response.AppointmentResponse;
import com.patient_booking.exceptions.AppointmentConflictException;
import com.patient_booking.exceptions.AppointmentNotFoundException;
import com.patient_booking.exceptions.InvalidAppointmentException;
import com.patient_booking.models.Appointment;
import com.patient_booking.models.DateRange;
import com.patient_booking.models.enums.AppointmentLength;
import com.patient_booking.models.enums.AppointmentStatus;
import com.patient_booking.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PaymentService paymentService;

    /** Books the appointment and its pending payment in one transaction. */
    @Transactional
    public UUID createAppointment(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        applyRequest(appointment, request);

        UUID appointmentId = appointmentRepository.save(appointment).getId();
        paymentService.createPendingPayment(appointmentId);
        return appointmentId;
    }

    @Transactional
    public UUID updateAppointment(UUID appointmentId, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
        applyRequest(appointment, request);
        // Managed entity: changes are flushed on commit, no explicit save needed.
        return appointment.getId();
    }

    public List<AppointmentResponse> getAllAppointments() {
        return toResponses(appointmentRepository.findAllByOrderByStartTimeAsc());
    }

    public List<AppointmentResponse> getTodayAppointments() {
        return getAppointmentsBetween(DateRange.ofDay(LocalDate.now()));
    }

    public List<AppointmentResponse> getTomorrowAppointments() {
        return getAppointmentsBetween(DateRange.ofDay(LocalDate.now().plusDays(1)));
    }

    public List<AppointmentResponse> getAppointmentsBetween(DateRange range) {
        return toResponses(appointmentRepository.findAllStartingBetween(
                range.startInclusive(), range.endExclusive()));
    }

    /** Shared by create and update: validates the slot and copies request fields onto the entity. */
    private void applyRequest(Appointment appointment, AppointmentRequest request) {
        LocalDateTime startTime = request.startTime();
        LocalDateTime endTime = startTime.plus(request.length().getDuration());

        validateSlotAlignment(startTime);
        if (appointmentRepository.existsOverlapping(startTime, endTime, appointment.getId())) {
            throw new AppointmentConflictException(startTime, endTime);
        }

        appointment.setFullName(request.fullName().trim());
        appointment.setNotes(request.notes());
        appointment.setPrice(request.price());
        appointment.setLength(request.length());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
    }

    /** Appointments must start on a slot boundary, e.g. 09:00 or 09:30. */
    private void validateSlotAlignment(LocalDateTime startTime) {
        boolean onSlotBoundary = startTime.getMinute() % AppointmentLength.SLOT_MINUTES == 0
                && startTime.getSecond() == 0
                && startTime.getNano() == 0;
        if (!onSlotBoundary) {
            throw new InvalidAppointmentException(
                    "startTime must align to a " + AppointmentLength.SLOT_MINUTES + "-minute slot");
        }
    }

    private List<AppointmentResponse> toResponses(List<Appointment> appointments) {
        return appointments.stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
