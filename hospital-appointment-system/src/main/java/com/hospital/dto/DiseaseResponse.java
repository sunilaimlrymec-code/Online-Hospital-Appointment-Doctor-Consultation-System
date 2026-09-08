package com.hospital.dto;

import com.hospital.entity.Disease;

public record DiseaseResponse(Integer id, String diseaseName) {
    public static DiseaseResponse from(Disease disease) {
        return new DiseaseResponse(disease.getId(), disease.getDiseaseName());
    }
}
