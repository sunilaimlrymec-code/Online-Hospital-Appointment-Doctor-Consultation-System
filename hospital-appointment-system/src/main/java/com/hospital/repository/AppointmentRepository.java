package com.hospital.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByUser_IdOrderByAppointmentDateAscAppointmentTimeAsc(Integer userId);

    List<Appointment> findByDoctor_IdOrderByAppointmentDateAscAppointmentTimeAsc(Integer doctorId);

    /** Used for the "you already booked a slot at this time" conflict check. */
    Optional<Appointment> findFirstByUser_IdAndAppointmentDateAndAppointmentTime(
            Integer userId, LocalDate date, LocalTime time);

    /** Used for the "doctor already booked at this time" conflict check. */
    Optional<Appointment> findFirstByDoctor_IdAndAppointmentDateAndAppointmentTime(
            Integer doctorId, LocalDate date, LocalTime time);

    List<Appointment> findByDoctor_IdAndAppointmentDate(Integer doctorId, LocalDate date);

    long countByDoctor_Id(Integer doctorId);
}
