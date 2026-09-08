package com.hospital.dto;

import com.hospital.entity.Doctor;

/** Safe, password-free projection of a Doctor returned by the API. */
public record DoctorResponse(Integer id, String name, String speciality,
                              Integer diseaseId, String diseaseName, String email,
                              String hospitalName, String hospitalAddress, String locationDetails) {
    public static DoctorResponse from(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpeciality(),
                doctor.getDisease().getId(),
                doctor.getDisease().getDiseaseName(),
                doctor.getEmail(),
                doctor.getHospitalName(),
                doctor.getHospitalAddress(),
                doctor.getLocationDetails());
    }
}
