package com.hospital.dto;

import com.hospital.entity.ConsultationReport;

import java.time.LocalDate;
import java.time.LocalTime;

/** Report projection, enriched with the appointment context the receipt page needs. */
public record ConsultationReportResponse(
        Integer appointmentId,
        String doctorName,
        String patientName,
        Integer age,
        String diseaseName,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String consultationMode,
        String status,
        String medicines,
        String suggestions
) {
    public static ConsultationReportResponse from(ConsultationReport report) {
        var appt = report.getAppointment();
        return new ConsultationReportResponse(
                appt.getId(),
                appt.getDoctor().getName(),
                appt.getUser().getName(),
                appt.getAge(),
                appt.getDoctor().getDisease().getDiseaseName(),
                appt.getAppointmentDate(),
                appt.getAppointmentTime(),
                appt.getConsultationMode(),
                report.getStatus(),
                report.getMedicines(),
                report.getSuggestions());
    }
}
