package com.hospital.service;

import com.hospital.dto.ReviewForm;
import com.hospital.entity.Appointment;
import com.hospital.entity.ConsultationReport;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.ConsultationReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultationReportService {

    private final ConsultationReportRepository reportRepository;
    private final AppointmentService appointmentService;

    public ConsultationReportService(ConsultationReportRepository reportRepository,
                                      AppointmentService appointmentService) {
        this.reportRepository = reportRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public ConsultationReport save(ReviewForm form) {
        Appointment appointment = appointmentService.getById(form.getAppointmentId());

        ConsultationReport report = reportRepository.findByAppointment_Id(appointment.getId())
                .orElseGet(ConsultationReport::new);

        report.setAppointment(appointment);
        report.setStatus(form.getStatus());
        report.setMedicines(form.getMedicines());
        report.setSuggestions(form.getSuggestions());

        ConsultationReport saved = reportRepository.save(report);

        // Keep the appointment's own lifecycle status in sync with the review outcome.
        appointmentService.updateStatus(appointment.getId(), form.getStatus());

        return saved;
    }

    public ConsultationReport getByAppointmentId(Integer appointmentId) {
        return reportRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No consultation report found for appointment: " + appointmentId));
    }

    public boolean isReviewSubmitted(Integer appointmentId) {
        return reportRepository.existsByAppointment_Id(appointmentId);
    }
}
