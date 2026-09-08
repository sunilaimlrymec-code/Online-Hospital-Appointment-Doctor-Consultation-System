package com.hospital.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The fixed set of consultation slots offered per day, preserved from the
 * original LoadAvailableSlotsServlet so existing bookings and UI behavior
 * stay consistent after the migration.
 */
public final class TimeSlotCatalog {

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("hh:mm a");

    private static final Map<LocalTime, LocalTime> SLOTS = new LinkedHashMap<>();

    static {
        SLOTS.put(LocalTime.of(9, 0), LocalTime.of(10, 0));
        SLOTS.put(LocalTime.of(10, 0), LocalTime.of(11, 0));
        SLOTS.put(LocalTime.of(11, 0), LocalTime.of(12, 0));
        SLOTS.put(LocalTime.of(12, 0), LocalTime.of(13, 0));
        SLOTS.put(LocalTime.of(14, 0), LocalTime.of(15, 0));
        SLOTS.put(LocalTime.of(15, 0), LocalTime.of(16, 0));
        SLOTS.put(LocalTime.of(16, 0), LocalTime.of(17, 0));
        SLOTS.put(LocalTime.of(17, 0), LocalTime.of(18, 0));
        SLOTS.put(LocalTime.of(18, 0), LocalTime.of(19, 0));
        SLOTS.put(LocalTime.of(19, 0), LocalTime.of(20, 0));
    }

    private TimeSlotCatalog() {
    }

    public static Map<LocalTime, LocalTime> all() {
        return SLOTS;
    }

    public static String label(LocalTime start, LocalTime end) {
        return start.format(DISPLAY) + " - " + end.format(DISPLAY);
    }
}
