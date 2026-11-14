package com.patient_booking.models.enums;

import lombok.Getter;

import java.time.Duration;
import java.util.Arrays;

@Getter
public enum AppointmentLength {
    HALF_HOUR(30, "30 min"),
    ONE_HOUR(60, "1 hr"),
    ONE_AND_A_HALF_HOUR(90, "1.5 hr"),
    TWO_HOUR(120, "2 hr");

    public static final int SLOT_MINUTES = 30;

    private final int minutes;
    private final String label;

    AppointmentLength(int minutes, String label) {
        this.minutes = minutes;
        this.label = label;
    }

    public Duration getDuration() {
        return Duration.ofMinutes(minutes);
    }

    /** Number of 30-minute slots this appointment takes up. */
    public int getSlotCount() {
        return minutes / SLOT_MINUTES;
    }

    public static AppointmentLength fromMinutes(int minutes) {
        return Arrays.stream(values())
                .filter(l -> l.minutes == minutes)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported appointment length: " + minutes + " min"));
    }
}