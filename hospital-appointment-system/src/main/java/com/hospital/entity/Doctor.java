package com.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Doctor account. Maps to the legacy "doctors" table; the disease_id
 * foreign key is now modeled as a proper JPA relationship instead of
 * a manually joined integer column.
 */
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Doctor name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Speciality is required")
    @Column(nullable = false, length = 150)
    private String speciality;

    @NotNull(message = "Please select a disease")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", nullable = false)
    private Disease disease;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 200)
    private String password; // BCrypt hash

    @NotBlank(message = "Hospital / clinic name is required")
    @Column(name = "hospital_name", nullable = false, length = 200)
    private String hospitalName;

    @NotBlank(message = "Hospital address is required")
    @Column(name = "hospital_address", nullable = false, length = 300)
    private String hospitalAddress;

    @Column(name = "location_details", length = 300)
    private String locationDetails; // e.g. "Block A, 3rd Floor, Room 204, opposite main lift"

    public Doctor() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public Disease getDisease() { return disease; }
    public void setDisease(Disease disease) { this.disease = disease; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public String getLocationDetails() { return locationDetails; }
    public void setLocationDetails(String locationDetails) { this.locationDetails = locationDetails; }
}
