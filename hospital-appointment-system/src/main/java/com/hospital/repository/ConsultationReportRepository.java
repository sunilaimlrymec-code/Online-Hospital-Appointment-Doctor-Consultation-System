package com.hospital.repository;

import com.hospital.entity.ConsultationReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationReportRepository extends JpaRepository<ConsultationReport, Integer> {

    Optional<ConsultationReport> findByAppointment_Id(Integer appointmentId);

    boolean existsByAppointment_Id(Integer appointmentId);
}
