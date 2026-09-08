package com.hospital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Patient-only API endpoints
        registry.addInterceptor(new SessionAuthInterceptor(SessionAuthInterceptor.Role.PATIENT))
                .addPathPatterns(
                        "/api/appointments",
                        "/api/appointments/**"
                )
                .excludePathPatterns(
                        "/api/appointments/slots" // available slots are public/browsable while booking
                );

        // Doctor-only API endpoints
        registry.addInterceptor(new SessionAuthInterceptor(SessionAuthInterceptor.Role.DOCTOR))
                .addPathPatterns(
                        "/api/doctor/appointments",
                        "/api/doctor/appointments/**",
                        "/api/reviews"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Static frontend is served from the same origin as the API, so no
        // cross-origin configuration is required by default. Left here as
        // an extension point if the frontend is ever split into its own app.
    }
}
