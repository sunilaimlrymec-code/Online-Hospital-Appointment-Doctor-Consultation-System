package com.hospital.service;

import com.hospital.entity.Disease;
import com.hospital.entity.Doctor;
import com.hospital.exception.DuplicateEmailException;
import com.hospital.exception.InvalidCredentialsException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DiseaseRepository;
import com.hospital.repository.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DiseaseRepository diseaseRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository,
                          DiseaseRepository diseaseRepository,
                          PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.diseaseRepository = diseaseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Doctor register(Doctor doctor, Integer diseaseId) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DuplicateEmailException("An account with this email already exists.");
        }

        Disease disease = diseaseRepository.findById(diseaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Selected disease not found: " + diseaseId));

        doctor.setDisease(disease);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    public Doctor authenticate(String email, String rawPassword) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No doctor account found for this email."));

        if (!passwordEncoder.matches(rawPassword, doctor.getPassword())) {
            throw new InvalidCredentialsException("Incorrect password.");
        }
        return doctor;
    }

    public Doctor getById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }

    public List<Doctor> getByDisease(Integer diseaseId) {
        return doctorRepository.findByDisease_Id(diseaseId);
    }
}
