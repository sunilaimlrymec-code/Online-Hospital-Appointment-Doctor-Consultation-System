package com.hospital.controller.api;

import com.hospital.dto.ConsultationReportResponse;
import com.hospital.dto.ReviewForm;
import com.hospital.service.ConsultationReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Equivalent to legacy addReview.jsp / saveReview.jsp / viewReport.jsp.
 * POST /api/reviews is doctor-only (see WebMvcConfig); the GET endpoints
 * are shared by both patients (viewing their own report) and doctors.
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewApiController {

    private final ConsultationReportService consultationReportService;

    public ReviewApiController(ConsultationReportService consultationReportService) {
        this.consultationReportService = consultationReportService;
    }

    @PostMapping
    public ResponseEntity<ConsultationReportResponse> save(@Valid @RequestBody ReviewForm form) {
        var report = consultationReportService.save(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConsultationReportResponse.from(report));
    }

    @GetMapping("/{appointmentId}")
    public ConsultationReportResponse view(@PathVariable Integer appointmentId) {
        return ConsultationReportResponse.from(consultationReportService.getByAppointmentId(appointmentId));
    }

    @GetMapping("/{appointmentId}/status")
    public Map<String, Boolean> status(@PathVariable Integer appointmentId) {
        return Map.of("submitted", consultationReportService.isReviewSubmitted(appointmentId));
    }
}
