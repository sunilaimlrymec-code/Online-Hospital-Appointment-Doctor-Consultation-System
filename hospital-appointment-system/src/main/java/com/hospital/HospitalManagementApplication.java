package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Hospital Appointment Scheduling & Telemedicine
 * Management System.
 *
 * Legacy origin: this application replaces a JDBC + Servlet + JSP stack
 * with a layered Spring Boot MVC application:
 *
 *   Entity -> Repository -> Service -> Controller -> Exception Handler
 */
@SpringBootApplication
public class HospitalManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }
}
