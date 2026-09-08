package com.hospital.dto;

/** Lightweight JSON projection returned by the "available slots" AJAX endpoint. */
public record SlotOption(String value, String label) {
}
