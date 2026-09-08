package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Form-backing object for the doctor's consultation review submission. */
public class ReviewForm {

    @NotNull
    private Integer appointmentId;

    @NotBlank(message = "Please select a consultation status")
    private String status; // e.g. "Completed", "Follow-up Required"

    private String medicines; // prescription

    private String suggestions;

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMedicines() { return medicines; }
    public void setMedicines(String medicines) { this.medicines = medicines; }

    public String getSuggestions() { return suggestions; }
    public void setSuggestions(String suggestions) { this.suggestions = suggestions; }
}
