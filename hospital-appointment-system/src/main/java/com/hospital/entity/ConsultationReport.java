package com.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The doctor's post-consultation review / report for a given appointment.
 * Maps to the legacy "consultation_reports" table.
 */
@Entity
@Table(name = "consultation_reports")
public class ConsultationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @NotBlank(message = "Please specify the consultation outcome")
    @Column(name = "status", nullable = false, length = 30)
    private String status; // e.g. "Completed", "Follow-up Required"

    @Column(length = 1000)
    private String medicines; // prescription

    @Column(length = 1000)
    private String suggestions;

    public ConsultationReport() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMedicines() { return medicines; }
    public void setMedicines(String medicines) { this.medicines = medicines; }

    public String getSuggestions() { return suggestions; }
    public void setSuggestions(String suggestions) { this.suggestions = suggestions; }
}
