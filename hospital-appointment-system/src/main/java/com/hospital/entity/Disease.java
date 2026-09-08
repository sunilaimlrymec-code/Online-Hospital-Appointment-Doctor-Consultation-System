package com.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Medical specialization / condition category used to route a patient
 * to the correct doctor. Maps to the legacy "diseases" table.
 */
@Entity
@Table(name = "diseases")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Disease name is required")
    @Column(name = "disease_name", nullable = false, length = 150)
    private String diseaseName;

    public Disease() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
}
