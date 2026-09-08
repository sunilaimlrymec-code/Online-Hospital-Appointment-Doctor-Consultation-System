package com.hospital.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Form-backing object for the "Book Appointment" page.
 * Keeps validation concerns out of the Appointment entity itself.
 */
public class AppointmentBookingForm {

    @NotNull(message = "Please select a disease")
    private Integer diseaseId;

    @NotNull(message = "Please select a doctor")
    private Integer doctorId;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

    @NotBlank(message = "Please select a gender")
    private String gender;

    @NotNull(message = "Please select an appointment date")
    private LocalDate date;

    @NotNull(message = "Please select a time slot")
    private LocalTime time;

    @NotBlank(message = "Please choose Online or In-Person")
    @Pattern(regexp = "ONLINE|OFFLINE", message = "Consultation mode must be ONLINE or OFFLINE")
    private String consultationMode;

    public Integer getDiseaseId() { return diseaseId; }
    public void setDiseaseId(Integer diseaseId) { this.diseaseId = diseaseId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getConsultationMode() { return consultationMode; }
    public void setConsultationMode(String consultationMode) { this.consultationMode = consultationMode; }
}
