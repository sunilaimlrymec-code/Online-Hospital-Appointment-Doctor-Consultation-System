package com.hospital.controller.api;

import com.hospital.dto.DiseaseResponse;
import com.hospital.dto.DoctorOption;
import com.hospital.service.DiseaseService;
import com.hospital.service.DoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * JSON replacements for the legacy LoadDoctorsByDiseaseServlet, which used
 * to hand-write raw HTML &lt;option&gt; fragments. The booking page's
 * JavaScript now renders the dropdown options client-side from this JSON.
 */
@RestController
public class LookupApiController {

    private final DiseaseService diseaseService;
    private final DoctorService doctorService;

    public LookupApiController(DiseaseService diseaseService, DoctorService doctorService) {
        this.diseaseService = diseaseService;
        this.doctorService = doctorService;
    }

    @GetMapping("/api/diseases")
    public List<DiseaseResponse> diseases() {
        return diseaseService.getAll().stream()
                .map(DiseaseResponse::from)
                .toList();
    }

    @GetMapping("/api/doctors")
    public List<DoctorOption> doctorsByDisease(@RequestParam Integer diseaseId) {
        return doctorService.getByDisease(diseaseId).stream()
                .map(d -> new DoctorOption(d.getId(), d.getName()))
                .toList();
    }
}
