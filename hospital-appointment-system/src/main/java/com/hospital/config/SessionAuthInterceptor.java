package com.hospital.config;

import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import com.hospital.exception.NotAuthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Guards patient-only and doctor-only API endpoints, mirroring the session
 * checks that used to be duplicated at the top of every legacy servlet.
 * The frontend is static HTML/CSS/JS, so instead of a server-side
 * redirect this simply rejects the request with 401; the calling page's
 * JavaScript is responsible for redirecting to the right login page.
 */
public class SessionAuthInterceptor implements HandlerInterceptor {

    public enum Role { PATIENT, DOCTOR }

    private final Role role;

    public SessionAuthInterceptor(Role role) {
        this.role = role;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();

        if (role == Role.PATIENT) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                throw new NotAuthenticatedException("Please log in as a patient to continue.");
            }
        } else {
            Doctor doctor = (Doctor) session.getAttribute("doctor");
            if (doctor == null) {
                throw new NotAuthenticatedException("Please log in as a doctor to continue.");
            }
        }
        return true;
    }
}
