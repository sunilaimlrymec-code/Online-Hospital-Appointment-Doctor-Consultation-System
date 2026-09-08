package com.hospital.dto;

/** Lightweight JSON projection returned by the "doctors by disease" AJAX endpoint. */
public record DoctorOption(Integer id, String name) {
}
