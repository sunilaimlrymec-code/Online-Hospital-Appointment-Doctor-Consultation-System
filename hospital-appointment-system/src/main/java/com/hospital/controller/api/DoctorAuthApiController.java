package com.hospital.controller.api;

import com.hospital.dto.DoctorResponse;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.RegisterDoctorRequest;
import com.hospital.entity.Doctor;
import com.hospital.exception.NotAuthenticatedException;
import com.hospital.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Doctor registration, login, logout and session lookup.
 * Equivalent to the legacy DoctorRegisterServlet / DoctorLoginServlet.
 */
@RestController
@RequestMapping("/api/doctor/auth")
public class DoctorAuthApiController {

    private final DoctorService doctorService;

    public DoctorAuthApiController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/register")
    public ResponseEntity<DoctorResponse> register(@Valid @RequestBody RegisterDoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSpeciality(request.getSpeciality());
        doctor.setEmail(request.getEmail());
        doctor.setPassword(request.getPassword());
        doctor.setHospitalName(request.getHospitalName());
        doctor.setHospitalAddress(request.getHospitalAddress());
        doctor.setLocationDetails(request.getLocationDetails());

        Doctor saved = doctorService.register(doctor, request.getDiseaseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(DoctorResponse.from(saved));
    }

    @PostMapping("/login")
    public DoctorResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        Doctor doctor = doctorService.authenticate(request.getEmail(), request.getPassword());
        servletRequest.getSession().setAttribute("doctor", doctor);
        return DoctorResponse.from(doctor);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public DoctorResponse me(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            throw new NotAuthenticatedException("No active doctor session.");
        }
        return DoctorResponse.from(doctor);
    }
}
