package com.hospital.exception;

/**
 * Thrown when an appointment cannot be booked because either the
 * patient already has a booking at that time, or the doctor's slot
 * is already taken by someone else.
 */
public class SlotUnavailableException extends RuntimeException {

    private final String doctorName;
    private final String time;

    public SlotUnavailableException(String message, String doctorName, String time) {
        super(message);
        this.doctorName = doctorName;
        this.time = time;
    }

    public String getDoctorName() { return doctorName; }
    public String getTime() { return time; }
}
