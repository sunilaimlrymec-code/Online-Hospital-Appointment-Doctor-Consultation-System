package com.hospital.service;

import com.hospital.dto.AppointmentBookingForm;
import com.hospital.dto.SlotOption;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserService userService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               DoctorRepository doctorRepository,
                               UserService userService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userService = userService;
    }

    /**
     * Books an appointment after running the same two conflict checks the
     * legacy BookAppointmentServlet performed:
     *   1) the patient does not already have a booking at that date/time
     *   2) the chosen doctor's slot is not already taken
     */
    @Transactional
    public Appointment book(Integer userId, AppointmentBookingForm form) {

        appointmentRepository.findFirstByUser_IdAndAppointmentDateAndAppointmentTime(
                        userId, form.getDate(), form.getTime())
                .ifPresent(existing -> {
                    throw new SlotUnavailableException(
                            "You already have an appointment at this time.",
                            existing.getDoctor().getName(),
                            formatTime(existing.getAppointmentTime()));
                });

        Doctor doctor = doctorRepository.findById(form.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + form.getDoctorId()));

        appointmentRepository.findFirstByDoctor_IdAndAppointmentDateAndAppointmentTime(
                        form.getDoctorId(), form.getDate(), form.getTime())
                .ifPresent(existing -> {
                    throw new SlotUnavailableException(
                            "This doctor is already booked at this time.",
                            doctor.getName(),
                            formatTime(form.getTime()));
                });

        User user = userService.getById(userId);

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDoctor(doctor);
        appointment.setAge(form.getAge());
        appointment.setGender(form.getGender());
        appointment.setAppointmentDate(form.getDate());
        appointment.setAppointmentTime(form.getTime());
        appointment.setConsultationMode(form.getConsultationMode());
        appointment.setStatus("Scheduled");

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getUserAppointments(Integer userId) {
        return appointmentRepository.findByUser_IdOrderByAppointmentDateAscAppointmentTimeAsc(userId);
    }

    public List<Appointment> getDoctorAppointments(Integer doctorId) {
        return appointmentRepository.findByDoctor_IdOrderByAppointmentDateAscAppointmentTimeAsc(doctorId);
    }

    public Appointment getById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + appointmentId));
    }

    public long countByDoctor(Integer doctorId) {
        return appointmentRepository.countByDoctor_Id(doctorId);
    }

    /** Updates the appointment's lifecycle status once a doctor submits a consultation review. */
    @Transactional
    public void updateStatus(Integer appointmentId, String status) {
        Appointment appointment = getById(appointmentId);
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }

    /**
     * Returns the bookable slots for a doctor on a given date: the fixed
     * daily catalog, minus already-booked times, minus any slot that has
     * already passed if the date is today.
     */
    public List<SlotOption> getAvailableSlots(Integer doctorId, LocalDate date) {

        Set<LocalTime> booked = appointmentRepository.findByDoctor_IdAndAppointmentDate(doctorId, date)
                .stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<SlotOption> options = new ArrayList<>();

        for (Map.Entry<LocalTime, LocalTime> slot : TimeSlotCatalog.all().entrySet()) {
            LocalTime start = slot.getKey();

            if (booked.contains(start)) {
                continue;
            }
            if (date.equals(today) && start.isBefore(now)) {
                continue;
            }

            options.add(new SlotOption(
                    start.format(DateTimeFormatter.ofPattern("HH:mm")),
                    TimeSlotCatalog.label(start, slot.getValue())));
        }

        return options;
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public LocalDateTime asDateTime(Appointment appointment) {
        return LocalDateTime.of(appointment.getAppointmentDate(), appointment.getAppointmentTime());
    }
}
