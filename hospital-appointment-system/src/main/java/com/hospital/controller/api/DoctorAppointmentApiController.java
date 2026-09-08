package com.hospital.controller.api;

import com.hospital.dto.AppointmentResponse;
import com.hospital.entity.Doctor;
import com.hospital.service.AppointmentService;
import com.hospital.service.ConsultationReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Doctor-facing appointment listing.
 * Equivalent to legacy DoctorDashboardServlet + doctorDashboard.jsp.
 * Guarded by SessionAuthInterceptor (doctor role).
 */
@RestController
@RequestMapping("/api/doctor/appointments")
public class DoctorAppointmentApiController {

    private final AppointmentService appointmentService;
    private final ConsultationReportService consultationReportService;

    public DoctorAppointmentApiController(AppointmentService appointmentService,
                                           ConsultationReportService consultationReportService) {
        this.appointmentService = appointmentService;
        this.consultationReportService = consultationReportService;
    }

    @GetMapping
    public Map<String, Object> myPatientAppointments(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");

        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctor.getId()).stream()
                .map(a -> AppointmentResponse.from(a, consultationReportService.isReviewSubmitted(a.getId())))
                .toList();

        return Map.of(
                "count", appointmentService.countByDoctor(doctor.getId()),
                "appointments", appointments
        );
    }
}
