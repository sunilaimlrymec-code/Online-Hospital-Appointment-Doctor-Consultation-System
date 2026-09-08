package com.hospital.dto;

import com.hospital.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

/** Full projection of an Appointment, safe to serialize to JSON. */
public record AppointmentResponse(
        Integer id,
        Integer doctorId,
        String doctorName,
        Integer diseaseId,
        String diseaseName,
        Integer userId,
        String userName,
        Integer age,
        String gender,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String consultationMode,
        String status,
        boolean reviewSubmitted,
        String hospitalName,
        String hospitalAddress,
        String locationDetails
) {
    public static AppointmentResponse from(Appointment a, boolean reviewSubmitted) {
        return new AppointmentResponse(
                a.getId(),
                a.getDoctor().getId(),
                a.getDoctor().getName(),
                a.getDoctor().getDisease().getId(),
                a.getDoctor().getDisease().getDiseaseName(),
                a.getUser().getId(),
                a.getUser().getName(),
                a.getAge(),
                a.getGender(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getConsultationMode(),
                a.getStatus(),
                reviewSubmitted,
                a.getDoctor().getHospitalName(),
                a.getDoctor().getHospitalAddress(),
                a.getDoctor().getLocationDetails());
    }
}
