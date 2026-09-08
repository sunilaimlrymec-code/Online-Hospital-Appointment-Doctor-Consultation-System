package com.hospital.controller.api;

import com.hospital.dto.AppointmentBookingForm;
import com.hospital.dto.AppointmentResponse;
import com.hospital.dto.SlotOption;
import com.hospital.entity.Appointment;
import com.hospital.entity.User;
import com.hospital.service.AppointmentService;
import com.hospital.service.ConsultationReportService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Appointment booking and listing for patients.
 * Equivalent to legacy BookAppointmentServlet + bookAppointment.jsp / viewAppointments.jsp.
 * Guarded by SessionAuthInterceptor (patient role) except for /slots, which
 * is safe to browse while filling out the booking form.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentApiController {

    private final AppointmentService appointmentService;
    private final ConsultationReportService consultationReportService;

    public AppointmentApiController(AppointmentService appointmentService,
                                     ConsultationReportService consultationReportService) {
        this.appointmentService = appointmentService;
        this.consultationReportService = consultationReportService;
    }

    @GetMapping
    public List<AppointmentResponse> myAppointments(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return appointmentService.getUserAppointments(user.getId()).stream()
                .map(a -> AppointmentResponse.from(a, consultationReportService.isReviewSubmitted(a.getId())))
                .toList();
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody AppointmentBookingForm form,
                                                      HttpSession session) {
        User user = (User) session.getAttribute("user");
        Appointment appointment = appointmentService.book(user.getId(), form);
        AppointmentResponse response = AppointmentResponse.from(appointment, false);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/slots")
    public List<SlotOption> availableSlots(@RequestParam Integer doctorId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getAvailableSlots(doctorId, date);
    }
}
